//package com.fsdm.test.bitcoinbj.listener;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fsdm.test.bitcoinbj.model.BitcoinWalletDAO;
//import lombok.AllArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.bitcoinj.core.Block;
//import org.bitcoinj.core.FilteredBlock;
//import org.bitcoinj.core.Peer;
//import org.bitcoinj.core.listeners.BlocksDownloadedEventListener;
//import org.bitcoinj.core.listeners.PeerConnectedEventListener;
//import org.bitcoinj.core.listeners.PeerDisconnectedEventListener;
//import org.springframework.stereotype.Component;
//
//import java.net.InetAddress;
//import java.net.UnknownHostException;
//
//@Component
//@Slf4j
//public class BitcoinPeerEventListener implements PeerConnectedEventListener, PeerDisconnectedEventListener, BlocksDownloadedEventListener {
//
//    private ObjectMapper objectMapper;
//    private InetAddress targetPeerAddress;
//
//    public BitcoinPeerEventListener(ObjectMapper objectMapper) {
//        this.objectMapper = objectMapper;
//        try {
//            // Define the target peer address you want to interact with
//            this.targetPeerAddress = InetAddress.getByName("seed.bitcoin.sipa.be");
//        } catch (UnknownHostException e) {
//            log.error("Failed to resolve the hostname for seed.bitcoin.sipa.be", e);
//            throw new RuntimeException("Failed to resolve hostname", e);
//        }
//    }
//
//    @Override
//    public void onPeerConnected(Peer peer, int peerCount) {
//        // Process events only if the connected peer is the targeted seed
//        if (peer.getAddress().getAddr().equals(targetPeerAddress)) {
//            log.info("Connected to the target seed peer: {}", peer);
//        }
//    }
//
//    @Override
//    public void onPeerDisconnected(Peer peer, int peerCount) {
//        // Handle disconnection only if it concerns the targeted seed
//        if (peer.getAddress().getAddr().equals(targetPeerAddress)) {
//            log.info("Disconnected from the target seed peer: {}", peer);
//        }
//    }
//
//    @Override
//    public void onBlocksDownloaded(Peer peer, Block block, FilteredBlock filteredBlock, int blocksLeft) {
//        // Process blocks only if they come from the targeted seed
//        if (peer.getAddress().getAddr().equals(targetPeerAddress)) {
//            try {
//                BitcoinWalletDAO bitcoinWallet = new BitcoinWalletDAO();
//                bitcoinWallet.setCreatedAt(block.getTime());
//                bitcoinWallet.setPeerInfo("Block received from target seed: " + block.getHashAsString());
//                bitcoinWallet.setTransactions(block.getTransactions().size());
//                bitcoinWallet.setNonce(block.getNonce());
//                bitcoinWallet.setDifficulty(block.getDifficultyTarget());
//
//                printAsJson(bitcoinWallet);
//            } catch (Exception e) {
//                log.error("Error processing block from target seed: {}", e.getMessage(), e);
//            }
//        }
//    }
//
//    private void printAsJson(BitcoinWalletDAO bitcoinWallet) {
//        try {
//            String json = objectMapper.writeValueAsString(bitcoinWallet);
//            log.info(json);
//        } catch (JsonProcessingException e) {
//            log.error("Error serializing BitcoinWallet to JSON: {}", e.getMessage());
//        }
//    }
//}
