package com.fsdm.test.bitcoinbj.service;

import com.fsdm.test.bitcoinbj.listener.BitcoinPeerEventListener;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.PeerGroup;
import org.bitcoinj.net.discovery.DnsDiscovery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class BitcoinNetworkService {

    private final NetworkParameters networkParameters;
    private PeerGroup peerGroup;

    @Autowired
    private BitcoinPeerEventListener bitcoinPeerEventListener;

    public BitcoinNetworkService(@Value("${bitcoin.network}") String network) {
        this.networkParameters = NetworkParameters.fromID(network);
        assert this.networkParameters != null;
    }

    @PostConstruct
    public void start() {
        // Set up the peer group with the network parameters and wallet
        this.peerGroup = new PeerGroup(networkParameters);
        this.peerGroup.addPeerDiscovery(new DnsDiscovery(networkParameters));

        // Attach event listeners
        this.peerGroup.addConnectedEventListener(bitcoinPeerEventListener);
        this.peerGroup.addDisconnectedEventListener(bitcoinPeerEventListener);

        // Start the peer group to begin listening to network events
        this.peerGroup.start();
        this.peerGroup.downloadBlockChain();
    }

    @PreDestroy
    public void stop() {
        if (this.peerGroup != null) {
            this.peerGroup.stop();
        }
    }
}
