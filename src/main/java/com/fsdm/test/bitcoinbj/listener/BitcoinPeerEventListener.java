package com.fsdm.test.bitcoinbj.listener;

import com.fsdm.test.bitcoinbj.model.transaction.BlockDAO;
import com.fsdm.test.bitcoinbj.model.transaction.TransactionDAO;
import com.fsdm.test.bitcoinbj.repository.BlockRepository;
import lombok.extern.slf4j.Slf4j;
import org.bitcoinj.core.Block;
import org.bitcoinj.core.FilteredBlock;
import org.bitcoinj.core.Peer;
import org.bitcoinj.core.listeners.BlocksDownloadedEventListener;
import org.bitcoinj.core.listeners.PeerConnectedEventListener;
import org.bitcoinj.core.listeners.PeerDisconnectedEventListener;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;

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
        BlockDAO blockDAO = getBlockDAO(block);
        blockRepository.save(blockDAO);
    }

    private static @NotNull BlockDAO getBlockDAO(Block block) {
        BlockDAO blockDAO = new BlockDAO();
        blockDAO.setHash(block.getHashAsString());
        blockDAO.setPreviousHash(block.getPrevBlockHash().toString());
        blockDAO.setNonce(block.getNonce());
        blockDAO.setDifficulty(block.getDifficultyTarget());
        blockDAO.setTimestamp(Instant.ofEpochSecond(block.getTimeSeconds()));
        blockDAO.setTransactions(new ArrayList<>());

        for (org.bitcoinj.core.Transaction tx : block.getTransactions()) {
            TransactionDAO domainTx = new TransactionDAO();
            domainTx.setTransactionId(tx.getHashAsString());
            blockDAO.getTransactions().add(domainTx);
        }
        return blockDAO;
    }
}

