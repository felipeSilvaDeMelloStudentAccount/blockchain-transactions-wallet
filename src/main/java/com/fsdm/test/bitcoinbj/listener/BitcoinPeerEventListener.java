package com.fsdm.test.bitcoinbj.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fsdm.test.bitcoinbj.model.BitcoinWalletDAO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bitcoinj.core.Peer;
import org.bitcoinj.core.listeners.PeerConnectedEventListener;
import org.bitcoinj.core.listeners.PeerDisconnectedEventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class BitcoinPeerEventListener implements PeerConnectedEventListener, PeerDisconnectedEventListener {

    private ObjectMapper objectMapper;

    @Override
    public void onPeerConnected(Peer peer, int peerCount) {
        // Safely log peer information
        if (peer != null && peer.getPeerVersionMessage() != null) {
            log.info("Connected to peer: {}, Total peers: {}", safePeerToString(peer), peerCount);
            BitcoinWalletDAO bitcoinWallet = new BitcoinWalletDAO();
            bitcoinWallet.setPeerInfo("Connected to peer: " + safePeerToString(peer));
            printAsJson(bitcoinWallet);

        } else {
            log.info("Connected to peer (details pending), Total peers: {}", peerCount);
        }
    }

    @Override
    public void onPeerDisconnected(Peer peer, int peerCount) {
        if (peer != null && peer.getPeerVersionMessage() != null) {
            log.info("Disconnected from peer: {}, Total peers: {}", safePeerToString(peer), peerCount);
        } else {
            log.info("Disconnected from peer (details were pending), Total peers: {}", peerCount);
        }
    }

    private String safePeerToString(Peer peer) {
        try {
            return peer.toString();
        } catch (NullPointerException e) {
            return "Peer information incomplete";
        }
    }

    private void printAsJson(BitcoinWalletDAO bitcoinWallet) {
        try {
            String json = objectMapper.writeValueAsString(bitcoinWallet);
            log.info(json);
        } catch (JsonProcessingException e) {
            log.error("Error serializing BitcoinWallet to JSON: {}", e.getMessage());
        }
    }
}