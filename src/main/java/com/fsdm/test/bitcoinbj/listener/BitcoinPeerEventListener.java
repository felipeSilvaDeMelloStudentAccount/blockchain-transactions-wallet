package com.fsdm.test.bitcoinbj.listener;

import lombok.extern.slf4j.Slf4j;
import org.bitcoinj.core.Block;
import org.bitcoinj.core.FilteredBlock;
import org.bitcoinj.core.Peer;
import org.bitcoinj.core.listeners.BlocksDownloadedEventListener;
import org.bitcoinj.core.listeners.PeerConnectedEventListener;
import org.bitcoinj.core.listeners.PeerDisconnectedEventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class BitcoinPeerEventListener implements PeerConnectedEventListener, PeerDisconnectedEventListener, BlocksDownloadedEventListener {

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
        // Process block and update database here if needed
    }
}
