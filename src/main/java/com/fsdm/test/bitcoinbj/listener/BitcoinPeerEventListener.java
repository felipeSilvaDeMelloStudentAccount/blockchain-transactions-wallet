package com.fsdm.test.bitcoinbj.listener;

import com.fsdm.test.bitcoinbj.model.transaction.BlockDAO;
import com.fsdm.test.bitcoinbj.model.transaction.TransactionDAO;
import com.fsdm.test.bitcoinbj.model.transaction.TransactionInput;
import com.fsdm.test.bitcoinbj.model.transaction.TransactionOutput;
import com.fsdm.test.bitcoinbj.repository.BlockRepository;
import lombok.extern.slf4j.Slf4j;
import org.bitcoinj.core.Block;
import org.bitcoinj.core.FilteredBlock;
import org.bitcoinj.core.Peer;
import org.bitcoinj.core.listeners.BlocksDownloadedEventListener;
import org.bitcoinj.core.listeners.PeerConnectedEventListener;
import org.bitcoinj.core.listeners.PeerDisconnectedEventListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class BitcoinPeerEventListener implements PeerConnectedEventListener, PeerDisconnectedEventListener, BlocksDownloadedEventListener {

    @Autowired
    private BlockRepository blockRepository;

    @Override
    public void onPeerConnected(Peer peer, int peerCount) {
        log.info("Connected to peer: {}", peer);
    }

    @Override
    public void onPeerDisconnected(Peer peer, int peerCount) {
        log.info("Disconnected from peer: {}", peer);
    }

    @Override
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

    private static BlockDAO getBlockDAO(Block block) {
        BlockDAO blockDAO = new BlockDAO();
        blockDAO.setHash(block.getHashAsString());
        blockDAO.setPreviousHash(block.getPrevBlockHash().toString());
        blockDAO.setNonce(block.getNonce());
        blockDAO.setDifficulty(block.getDifficultyTarget());
        blockDAO.setTimestamp(Instant.ofEpochSecond(block.getTimeSeconds()));
        blockDAO.setTransactions(new ArrayList<>());

        for (org.bitcoinj.core.Transaction tx : block.getTransactions()) {
            log.info("Transaction: {}", tx.getHashAsString());
            TransactionDAO domainTx = new TransactionDAO();
            domainTx.setTransactionId(tx.getHashAsString());
            domainTx.setBlockDAO(blockDAO);
            domainTx.setInputs(getTransactionInputs(tx.getInputs(), domainTx));
            domainTx.setOutputs(getTransactionOutputs(tx.getOutputs(), domainTx));
            blockDAO.getTransactions().add(domainTx);
        }
        return blockDAO;
    }

    private static List<TransactionInput> getTransactionInputs(List<org.bitcoinj.core.TransactionInput> btcInputs, TransactionDAO domainTx) {
        List<TransactionInput> inputs = new ArrayList<>();
        for (org.bitcoinj.core.TransactionInput btcInput : btcInputs) {
            TransactionInput input = new TransactionInput();
            input.setSourceTransactionId(btcInput.getOutpoint().toString());
            input.setOutputIndex(btcInput.getIndex());
            input.setScriptSig(btcInput.getScriptSig().toString());
            input.setTransactionDAO(domainTx);
            inputs.add(input);
        }
        return inputs;
    }

    private static List<TransactionOutput> getTransactionOutputs(List<org.bitcoinj.core.TransactionOutput> btcOutputs, TransactionDAO domainTx) {
        List<TransactionOutput> outputs = new ArrayList<>();
        for (org.bitcoinj.core.TransactionOutput btcOutput : btcOutputs) {
            TransactionOutput output = new TransactionOutput();
            output.setValue(btcOutput.getValue().toBtc());
            output.setScriptPubKey(btcOutput.getScriptPubKey().toString());
            output.setTransactionDAO(domainTx);
            outputs.add(output);
        }
        return outputs;
    }

    private boolean verifyBlockHash(Block block, BlockDAO blockDAO) {
        // Implement hash verification logic here
        return block.getHashAsString().equals(blockDAO.getHash());
    }
}
