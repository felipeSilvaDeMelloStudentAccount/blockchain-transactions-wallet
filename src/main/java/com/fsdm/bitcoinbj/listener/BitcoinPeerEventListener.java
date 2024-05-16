package com.fsdm.bitcoinbj.listener;

import com.fsdm.bitcoinbj.model.transaction.BlockDAO;
import com.fsdm.bitcoinbj.model.transaction.TransactionDAO;
import com.fsdm.bitcoinbj.model.transaction.TransactionInput;
import com.fsdm.bitcoinbj.model.transaction.TransactionOutput;
import com.fsdm.bitcoinbj.repository.BlockRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bitcoinj.core.Block;
import org.bitcoinj.core.FilteredBlock;
import org.bitcoinj.core.Peer;
import org.bitcoinj.core.listeners.BlocksDownloadedEventListener;
import org.bitcoinj.core.listeners.PeerConnectedEventListener;
import org.bitcoinj.core.listeners.PeerDisconnectedEventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Class Overview: This class listens for events from Bitcoin peers, such as when peers connect, disconnect, and when blocks are downloaded.
 * It processes the downloaded blocks and transactions, and saves them to a PostgreSQL database.
 * implements PeerConnectedEventListener, PeerDisconnectedEventListener, and BlocksDownloadedEventListener
 * to respond to Bitcoin network events.
 *
 */
@Component
@Slf4j
@AllArgsConstructor
public class BitcoinPeerEventListener implements PeerConnectedEventListener, PeerDisconnectedEventListener, BlocksDownloadedEventListener {

    private BlockRepository blockRepository;

    /**
     * Handles the event when a peer is connected.
     *
     * @param peer      the connected peer
     * @param peerCount the total number of connected peers
     */
    @Override
    public void onPeerConnected(Peer peer, int peerCount) {
        log.info("Connected to peer: {}", peer);
    }

    /**
     * Handles the event when a peer is disconnected.
     *
     * @param peer      the disconnected peer
     * @param peerCount the total number of connected peers
     */
    @Override
    public void onPeerDisconnected(Peer peer, int peerCount) {
        log.info("Disconnected from peer: {}", peer);
    }


    /**
     * Handles the event when blocks are downloaded from a peer. This method is annotated with @Transactional to ensure
     * that the database operations are performed within a transaction context.
     *
     * @param peer          the peer from which the block was downloaded
     * @param block         the downloaded block
     * @param filteredBlock the filtered block
     * @param blocksLeft    the number of blocks left to download
     */
    @Override
    @Transactional
    public void onBlocksDownloaded(Peer peer, Block block, FilteredBlock filteredBlock, int blocksLeft) {
        log.info("Block downloaded: {} from peer: {}", block.getHashAsString(), peer);
        log.info("Block details: Nonce: {}, Difficulty: {}, Transactions: {}", block.getNonce(), block.getDifficultyTarget(), block.getTransactions().size());

        BlockDAO blockDAO = getBlockDAO(block);
        if (verifyBlockHash(block, blockDAO)) {
            try {
                blockRepository.save(blockDAO);
                log.info("Block saved: {}", block.getHashAsString());
            } catch (Exception e) {
                log.error("Error saving block: {}", e.getMessage(), e);
            }
        } else {
            log.error("Block hash verification failed for block: {}", block.getHashAsString());
        }
    }

    /**
     * Converts a BitcoinJ Block object to a BlockDAO object for database storage.
     *
     * @param block the BitcoinJ block
     * @return the BlockDAO object
     */
    private static BlockDAO getBlockDAO(Block block) {
        BlockDAO blockDAO = new BlockDAO();
        blockDAO.setHash(block.getHashAsString());
        blockDAO.setPreviousHash(block.getPrevBlockHash().toString());
        blockDAO.setNonce(block.getNonce());
        blockDAO.setDifficulty(block.getDifficultyTarget());
        blockDAO.setTimestamp(Instant.ofEpochSecond(block.getTimeSeconds()));
        blockDAO.setTransactions(new ArrayList<>());

        log.info("Processing block: {}", block.getHashAsString());

        for (org.bitcoinj.core.Transaction tx : block.getTransactions()) {
            log.info("Processing transaction: {}", tx.getTxId().toString());
            TransactionDAO domainTx = new TransactionDAO();
            domainTx.setTransactionId(tx.getTxId().toString());
            domainTx.setBlockDAO(blockDAO);
            domainTx.setInputs(getTransactionInputs(tx.getInputs(), domainTx));
            domainTx.setOutputs(getTransactionOutputs(tx.getOutputs(), domainTx));
            blockDAO.getTransactions().add(domainTx);
        }
        return blockDAO;
    }

    /**
     * Converts a list of BitcoinJ TransactionInput objects to a list of TransactionInput objects for database storage.
     *
     * @param btcInputs the list of BitcoinJ transaction inputs
     * @param domainTx  the TransactionDAO object to which the inputs belong
     * @return the list of TransactionInput objects
     */
    private static List<TransactionInput> getTransactionInputs(List<org.bitcoinj.core.TransactionInput> btcInputs, TransactionDAO domainTx) {
        List<TransactionInput> inputs = new ArrayList<>();
        for (org.bitcoinj.core.TransactionInput btcInput : btcInputs) {
            TransactionInput input = new TransactionInput();
            input.setSourceTransactionId(btcInput.getOutpoint().toString());
            input.setOutputIndex(btcInput.getIndex());
            input.setScriptSig(btcInput.getScriptSig().toString());
            input.setTransactionDAO(domainTx);
            inputs.add(input);
            log.info("Processed transaction input: {}", input);
        }
        return inputs;
    }

    /**
     * Converts a list of BitcoinJ TransactionOutput objects to a list of TransactionOutput objects for database storage.
     *
     * @param btcOutputs the list of BitcoinJ transaction outputs
     * @param domainTx   the TransactionDAO object to which the outputs belong
     * @return the list of TransactionOutput objects
     */
    private static List<TransactionOutput> getTransactionOutputs(List<org.bitcoinj.core.TransactionOutput> btcOutputs, TransactionDAO domainTx) {
        List<TransactionOutput> outputs = new ArrayList<>();
        for (org.bitcoinj.core.TransactionOutput btcOutput : btcOutputs) {
            TransactionOutput output = new TransactionOutput();
            output.setValue(btcOutput.getValue().toBtc());
            output.setScriptPubKey(btcOutput.getScriptPubKey().toString());
            output.setTransactionDAO(domainTx);
            outputs.add(output);
            log.info("Processed transaction output: {}", output);
        }
        return outputs;
    }

    /**
     * Verifies that the hash of a BitcoinJ Block matches the hash stored in a BlockDAO object.
     *
     * @param block    the BitcoinJ block
     * @param blockDAO the BlockDAO object
     * @return true if the hashes match, false otherwise
     */
    private boolean verifyBlockHash(Block block, BlockDAO blockDAO) {
        boolean isValid = block.getHashAsString().equals(blockDAO.getHash());
        log.info("Block hash verification for block {}: {}", block.getHashAsString(), isValid);
        return isValid;
    }
}
