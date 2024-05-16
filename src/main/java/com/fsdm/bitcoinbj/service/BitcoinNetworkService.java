package com.fsdm.bitcoinbj.service;

import com.fsdm.bitcoinbj.listener.BitcoinPeerEventListener;
import com.fsdm.bitcoinbj.model.transaction.BlockDAO;
import com.fsdm.bitcoinbj.model.transaction.TransactionDAO;
import com.fsdm.bitcoinbj.model.transaction.TransactionInput;
import com.fsdm.bitcoinbj.model.transaction.TransactionOutput;
import com.fsdm.bitcoinbj.repository.BlockRepository;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.bitcoinj.core.BlockChain;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.PeerAddress;
import org.bitcoinj.core.PeerGroup;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.net.NioClientManager;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.store.BlockStore;
import org.bitcoinj.store.MemoryBlockStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * This class manages the connection to the Bitcoin network.
 * It connects to the network, listens for new blocks, processes them,
 * and saves the block and transaction data to a PostgreSQL database.
 */
@Service
@Slf4j
public class BitcoinNetworkService {
    // The target node address for the Bitcoin network.
    @Value("${bitcoin.targetNode}")
    private String targetNode;

    //The port number for the Bitcoin network.
    @Value("${bitcoin.port}")
    private int port;

    //The peer group for managing connections to Bitcoin peers.
    private PeerGroup peerGroup;

    //The NioClientManager for managing network connections.
    private final NioClientManager clientManager = new NioClientManager();

    //Event listener for peer-related events.
    @Autowired
    private BitcoinPeerEventListener bitcoinPeerEventListener;

    //A Spring Data JPA repository for performing CRUD operations on BlockDAO objects.
    @Autowired
    private BlockRepository blockRepository;

    /**
     * Initializes the service by connecting to the Bitcoin network.
     * This method is called after the bean is constructed.
     */
    @PostConstruct
    public void start() {
        connectToNetwork();
    }

    /**
     * Connects to the Bitcoin network asynchronously with retry logic.
     *
     * @return a CompletableFuture that completes when the connection is established
     */
    @Async
    public CompletableFuture<Void> connectToNetwork() {
        int maxRetries = 5;
        int attempt = 0;

        while (attempt < maxRetries) {
            try {
                attempt++;
                NetworkParameters networkParameters = MainNetParams.get();
                BlockStore blockStore = new MemoryBlockStore(networkParameters);
                BlockChain blockChain = new BlockChain(networkParameters, blockStore);

                this.peerGroup = new PeerGroup(networkParameters, blockChain);
                InetAddress seedAddress = InetAddress.getByName(targetNode);
                this.peerGroup.addAddress(new PeerAddress(networkParameters, seedAddress, port));

                this.peerGroup.addConnectedEventListener(bitcoinPeerEventListener);
                this.peerGroup.addDisconnectedEventListener(bitcoinPeerEventListener);
                this.peerGroup.addBlocksDownloadedEventListener((peer, block, filteredBlock, blocksLeft) -> saveBlock(block));

                this.peerGroup.start();
                CompletableFuture.runAsync(() -> {
                    try {
                        this.peerGroup.downloadBlockChain();
                    } catch (Exception e) {
                        log.error("Error downloading blockchain: {}", e.getMessage(), e);
                    }
                });

                log.info("Bitcoin network service started, connected only to seed.bitcoin.sipa.be.");
                break;
            } catch (Exception e) {
                log.error("Error connecting to Bitcoin network (attempt {}/{}): {}", attempt, maxRetries, e.getMessage());
                if (attempt == maxRetries) {
                    throw new RuntimeException("Failed to connect to Bitcoin network after " + maxRetries + " attempts", e);
                }
                try {
                    Thread.sleep(5000); // Wait before retrying
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        return CompletableFuture.completedFuture(null);
    }

    /**
     * Stops the service and disconnects from the Bitcoin network.
     * This method is called before the bean is destroyed.
     */
    @PreDestroy
    public void stop() {
        if (this.peerGroup != null) {
            this.peerGroup.stop();
        }
        clientManager.stopAsync();
        clientManager.awaitTerminated();
        log.info("Bitcoin network service stopped.");
    }

    /**
     * Saves a downloaded Bitcoin block to the database.
     *
     * @param block the BitcoinJ Block object
     */
    private void saveBlock(org.bitcoinj.core.Block block) {
        BlockDAO blockDAO = BlockDAO.builder()
                .hash(block.getHashAsString())
                .previousHash(block.getPrevBlockHash().toString())
                .nonce(block.getNonce())
                .difficulty(block.getDifficultyTarget())
                .timestamp(block.getTime().toInstant())
                .build();

        List<TransactionDAO> transactionDAOs = new ArrayList<>();
        for (Transaction transaction : block.getTransactions()) {
            TransactionDAO transactionDAO = TransactionDAO.builder()
                    .transactionId(transaction.getTxId().toString())
                    .blockDAO(blockDAO)
                    .build();

            List<TransactionInput> inputs = transaction.getInputs().stream().map(input -> TransactionInput.builder()
                    .sourceTransactionId(input.getOutpoint().getHash().toString())
                    .outputIndex((int) input.getOutpoint().getIndex())
                    .scriptSig(input.getScriptSig().toString())
                    .transactionDAO(transactionDAO)
                    .build()).collect(Collectors.toList());

            List<TransactionOutput> outputs = transaction.getOutputs().stream().map(output -> TransactionOutput.builder()
                    .value(output.getValue().toBtc())
                    .scriptPubKey(output.getScriptPubKey().toString())
                    .transactionDAO(transactionDAO)
                    .build()).collect(Collectors.toList());

            transactionDAO.setInputs(inputs);
            transactionDAO.setOutputs(outputs);

            TransactionDAO apply = transactionDAO;
            transactionDAOs.add(apply);
        }

        blockDAO.setTransactions(transactionDAOs);
        blockRepository.save(blockDAO);
    }
}
