package com.fsdm.test.bitcoinbj.service;

import com.fsdm.test.bitcoinbj.listener.BitcoinPeerEventListener;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class BitcoinNetworkService {

    private static final String TARGET_NODE = "seed.bitcoin.sipa.be";
    private static final int PORT = 8333;
    private PeerGroup peerGroup;
    private BlockChain blockChain;
    private final NioClientManager clientManager = new NioClientManager();

    @Autowired
    private BitcoinPeerEventListener bitcoinPeerEventListener;

    @PostConstruct
    public void start() {
        startAsync();
    }

    @Async
    public CompletableFuture<Void> startAsync() {
        try {
            NetworkParameters networkParameters = MainNetParams.get();
            BlockStore blockStore = new MemoryBlockStore(networkParameters);
            this.blockChain = new BlockChain(networkParameters, blockStore);

            this.peerGroup = new PeerGroup(networkParameters, blockChain);
            InetAddress seedAddress = InetAddress.getByName(TARGET_NODE);
            this.peerGroup.addAddress(new PeerAddress(networkParameters, seedAddress, PORT));

            this.peerGroup.addConnectedEventListener(bitcoinPeerEventListener);
            this.peerGroup.addDisconnectedEventListener(bitcoinPeerEventListener);
            this.peerGroup.addBlocksDownloadedEventListener(bitcoinPeerEventListener);

            this.peerGroup.start();

            // Download blockchain in a separate thread
            CompletableFuture.runAsync(() -> {
                try {
                    this.peerGroup.downloadBlockChain();
                } catch (Exception e) {
                    log.error("Error downloading blockchain: {}", e.getMessage(), e);
                }
            });

            log.info("Bitcoin network service started, connected only to seed.bitcoin.sipa.be.");
        } catch (Exception e) {
            log.error("Error starting Bitcoin network service: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to start Bitcoin network service", e);
        }
        return CompletableFuture.completedFuture(null);
    }

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

