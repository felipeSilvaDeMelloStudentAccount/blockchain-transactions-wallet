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

import java.time.Instant;
import java.util.Date;
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
        BitcoinWallet bitcoinWallet = buildBitcoinWallet(wallet);
        //Save to the wallet
        bitcoinWalletRepository.save(bitcoinWallet);
        return bitcoinWallet;
    }

    private BitcoinWallet buildBitcoinWallet(Wallet wallet) {
        DeterministicSeed keyChainSeed = wallet.getKeyChainSeed();
        String seedWords = Joiner.on(" ").join(Objects.requireNonNull(keyChainSeed.getMnemonicCode()));

        // Retrieve the wallet details
        Address address = wallet.currentReceiveAddress();
        ECKey keyFromAddress = wallet.findKeyFromAddress(address);

        // Get the address and keys
        String bitcoinAddress = address.toString();
        String privateKeyAsHex = keyFromAddress.getPrivateKeyAsHex();
        String publicKeyAsHex = keyFromAddress.getPublicKeyAsHex();

        return BitcoinWallet.builder()
                .address(bitcoinAddress)
                .balance(wallet.getBalance().getValue())
                .privateKey(privateKeyAsHex)
                .publicKey(publicKeyAsHex)
                .seedWords(seedWords)
                .createdAt(convertEpochToDate(wallet.getEarliestKeyCreationTime()))
                .updatedAt(convertEpochToDate(wallet.getLastBlockSeenTimeSecs()))
                .build();

    }
    
    public static Date convertEpochToDate(long epochSeconds) {
        // Convert the epoch seconds directly to an Instant
        Instant instant = Instant.ofEpochSecond(epochSeconds);
        return Date.from(instant);
    }
}