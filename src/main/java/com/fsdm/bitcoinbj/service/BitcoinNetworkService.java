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
        connectToNetwork();
    }

    @Async
    public CompletableFuture<Void> connectToNetwork() {
        int maxRetries = 5;
        int attempt = 0;

        while (attempt < maxRetries) {
            try {
                attempt++;
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
