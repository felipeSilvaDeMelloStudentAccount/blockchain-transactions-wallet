package com.fsdm.bitcoinbj.service;

import com.fsdm.bitcoinbj.listener.BitcoinPeerEventListener;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.bitcoinj.core.BlockChain;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.PeerAddress;
import org.bitcoinj.core.PeerGroup;
import org.bitcoinj.net.NioClientManager;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.store.BlockStore;
import org.bitcoinj.store.MemoryBlockStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.util.concurrent.CompletableFuture;

/**
 * This class manages the connection to the Bitcoin network.
 * It connects to the network, listens for new blocks, processes them,
 * and saves the block and transaction data to a PostgreSQL database.
 */
@Service
@Slf4j
public class BitcoinNetworkService {
    // The target node address for the Bitcoin network.
    private String targetNode;

    //The port number for the Bitcoin network.
    private int port;

    //The peer group for managing connections to Bitcoin peers.
    private PeerGroup peerGroup;

    //The NioClientManager for managing network connections.
    private final NioClientManager clientManager = new NioClientManager();

    //Event listener for peer-related events.

    private BitcoinPeerEventListener bitcoinPeerEventListener;


    private BlockService blockService;

    public BitcoinNetworkService(@Value("${bitcoin.targetNode}") String targetNode, @Value("${bitcoin.port}") int port, BlockService blockService, BitcoinPeerEventListener bitcoinPeerEventListener) {
        this.targetNode = targetNode;
        this.port = port;
        this.blockService = blockService;
        this.bitcoinPeerEventListener = bitcoinPeerEventListener;
    }

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
                this.peerGroup.addBlocksDownloadedEventListener((peer, block, filteredBlock, blocksLeft) -> blockService.saveBlock(block));

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
}
