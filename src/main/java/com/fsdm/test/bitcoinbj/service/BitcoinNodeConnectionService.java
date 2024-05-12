package com.fsdm.test.bitcoinbj.service;

import lombok.extern.slf4j.Slf4j;
import org.bitcoinj.core.*;
import org.bitcoinj.net.NioClientManager;
import org.bitcoinj.params.MainNetParams;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.InetSocketAddress;

@Service
@Slf4j
public class BitcoinNodeConnectionService {
    private static final String TARGET_NODE = "seed.bitcoin.sipa.be";
    private static final int PORT = 8333; // Default port for Bitcoin mainnet
    private static final NetworkParameters NETWORK_PARAMETERS = MainNetParams.get();

    private final NioClientManager clientManager = new NioClientManager();

    public void startConnection() {
        try {
            InetAddress targetAddress = InetAddress.getByName(TARGET_NODE);
            InetSocketAddress socketAddress = new InetSocketAddress(targetAddress, PORT);

            // Create the VersionMessage with correct user-agent details
            VersionMessage versionMessage = new VersionMessage(NETWORK_PARAMETERS, 1);
            versionMessage.appendToSubVer("NodeConnectionService", 1, null);

            // Set up a BlockChain and a MemoryBlockStore although they are minimally used here
            BlockChain chain = new BlockChain(NETWORK_PARAMETERS, new MemoryBlockStore(NETWORK_PARAMETERS));

            // Create a PeerGroup to manage our single peer connection
            PeerGroup peerGroup = new PeerGroup(NETWORK_PARAMETERS, chain);
            peerGroup.addAddress(new PeerAddress(NETWORK_PARAMETERS, targetAddress, PORT));

            // Connect the peer and set up listeners
            peerGroup.start();
            peerGroup.waitForPeers(1).get();

            Peer peer = peerGroup.getConnectedPeers().get(0);

            // Add event listeners for peer interaction
            peer.addConnectedEventListener((peer1, peerCount) -> {
                log.info("Connected to {}: {}", TARGET_NODE, peer1);
                // Successfully connected, send the version message
                peer1.sendMessage(versionMessage);
            });

            peer.addBlocksDownloadedEventListener((peer1, block, filteredBlock, blocksLeft) -> {
                log.info("Block downloaded: " + block.getHashAsString());
            });

            peer.addChainDownloadStartedEventListener((peer1, blocksLeft) -> {
                log.info("Chain download started with {} blocks left", blocksLeft);
            });
        } catch (Exception e) {
            log.error("Error connecting to {}: {}", TARGET_NODE, e.getMessage(), e);
        }
    }

    public void stopConnection() {
        clientManager.stopAsync();
        clientManager.awaitTerminated();
        log.info("Connection to node " + TARGET_NODE + " has been closed.");
    }
}
