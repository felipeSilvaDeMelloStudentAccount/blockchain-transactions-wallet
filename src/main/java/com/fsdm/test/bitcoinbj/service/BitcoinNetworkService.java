//package com.fsdm.test.bitcoinbj.service;
//
//import com.fsdm.test.bitcoinbj.listener.BitcoinPeerEventListener;
//import jakarta.annotation.PostConstruct;
//import jakarta.annotation.PreDestroy;
//import lombok.extern.slf4j.Slf4j;
//import org.bitcoinj.core.*;
//import org.bitcoinj.params.MainNetParams;
//import org.bitcoinj.store.MemoryBlockStore;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.net.InetAddress;
//
//@Service
//@Slf4j
//public class BitcoinNetworkService {
//
//    private PeerGroup peerGroup;
//    private BlockChain blockChain;
//
//    @Autowired
//    private BitcoinPeerEventListener bitcoinPeerEventListener;
//
//    public BitcoinNetworkService() {
//    }
//
//    @PostConstruct
//    public void start() {
//        try {
//            // Initialize the BlockChain with MainNet parameters and MemoryBlockStore
//            NetworkParameters networkParameters = MainNetParams.get();
//            MemoryBlockStore blockStore = new MemoryBlockStore(networkParameters);
//            this.blockChain = new BlockChain(networkParameters, blockStore);
//
//            // Set up the peer group with the network parameters and blockchain
//            this.peerGroup = new PeerGroup(networkParameters, blockChain);
//
//            // Manually add the seed node 'seed.bitcoin.sipa.be'
//            InetAddress seedAddress = InetAddress.getByName("seed.bitcoin.sipa.be");
//            this.peerGroup.addAddress(new PeerAddress(networkParameters, seedAddress, networkParameters.getPort()));
//
//            // Attach event listeners to manage peer connections and blockchain data
//            this.peerGroup.addConnectedEventListener(bitcoinPeerEventListener);
//            this.peerGroup.addDisconnectedEventListener(bitcoinPeerEventListener);
//            this.peerGroup.addBlocksDownloadedEventListener(bitcoinPeerEventListener);
//
//            // Start the peer group to initiate network activities
//            this.peerGroup.start();
//            this.peerGroup.downloadBlockChain();
//            log.info("Bitcoin network service started, connected only to seed.bitcoin.sipa.be.");
//        } catch (Exception e) {
//            log.error("Error starting Bitcoin network service: {}", e.getMessage(), e);
//            throw new RuntimeException("Failed to start Bitcoin network service", e);
//        }
//    }
//
//    @PreDestroy
//    public void stop() {
//        // Gracefully stop the peer group and release resources
//        if (this.peerGroup != null) {
//            this.peerGroup.stop();
//            log.info("Bitcoin network service stopped.");
//        }
//    }
//}
