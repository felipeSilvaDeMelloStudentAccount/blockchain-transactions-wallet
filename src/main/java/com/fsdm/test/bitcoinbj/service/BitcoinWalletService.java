package com.fsdm.test.bitcoinbj.service;

import com.fsdm.test.bitcoinbj.model.BitcoinWallet;
import com.fsdm.test.bitcoinbj.repository.BitcoinWalletRepository;
import com.google.common.base.Joiner;
import org.bitcoinj.core.Address;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.script.Script;
import org.bitcoinj.wallet.DeterministicSeed;
import org.bitcoinj.wallet.Wallet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class BitcoinWalletService {


    private final String network;

    private final BitcoinWalletRepository bitcoinWalletRepository;

    public BitcoinWalletService(@Value("${bitcoin.network}") String network, BitcoinWalletRepository bitcoinWalletRepository) {
        this.network = network;
        this.bitcoinWalletRepository = bitcoinWalletRepository;
    }

    public BitcoinWallet createWallet() {
        final NetworkParameters networkParameters = NetworkParameters.fromID(network);
        assert networkParameters != null;
        // Change ScriptType from P2PK to P2PKH or P2WPKH
        Wallet wallet = Wallet.createDeterministic(networkParameters, Script.ScriptType.P2PKH);
        DeterministicSeed keyChainSeed = wallet.getKeyChainSeed();
        String seedWords = Joiner.on(" ").join(Objects.requireNonNull(keyChainSeed.getMnemonicCode()));

        BitcoinWallet bitcoinWallet = new BitcoinWallet();

        // Retrieve the wallet details
        Address address = wallet.currentReceiveAddress();
        ECKey keyFromAddress = wallet.findKeyFromAddress(address);

        // Get the address and keys
        String bitcoinAddress = address.toString();
        String privateKeyAsHex = keyFromAddress.getPrivateKeyAsHex();
        String publicKeyAsHex = keyFromAddress.getPublicKeyAsHex();

        // Set values for saving to database
        bitcoinWallet.setAddress(bitcoinAddress);
        bitcoinWallet.setPrivateKey(privateKeyAsHex);
        bitcoinWallet.setPublicKey(publicKeyAsHex);
        bitcoinWallet.setSeedWords(seedWords);

        // Save wallet locally to db
        bitcoinWalletRepository.save(bitcoinWallet);

        return bitcoinWallet;
    }
}