//package com.fsdm.test.bitcoinbj.service;
//
//import com.fsdm.test.bitcoinbj.model.BitcoinWallet;
//import com.fsdm.test.bitcoinbj.repository.BitcoinWalletRepository;
//import com.google.common.base.Joiner;
//import org.bitcoinj.core.Address;
//import org.bitcoinj.core.ECKey;
//import org.bitcoinj.core.NetworkParameters;
//import org.bitcoinj.script.Script;
//import org.bitcoinj.wallet.DeterministicSeed;
//import org.bitcoinj.wallet.Wallet;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//import java.time.Instant;
//import java.util.Date;
//import java.util.Objects;
//
//@Service
//public class BitcoinWalletService {
//
//    private final NetworkParameters networkParameters;
//    private final BitcoinWalletRepository bitcoinWalletRepository;
//
//    public BitcoinWalletService(@Value("${bitcoin.network}") String network, BitcoinWalletRepository bitcoinWalletRepository) {
//        this.networkParameters = NetworkParameters.fromID(network);
//        if (this.networkParameters == null) {
//            throw new IllegalArgumentException("Unsupported Bitcoin network ID: " + network);
//        }
//        this.bitcoinWalletRepository = bitcoinWalletRepository;
//    }
//
//    public BitcoinWallet createWallet() {
//        try {
//            // Create a new deterministic wallet using P2PKH script type
//            Wallet wallet = Wallet.createDeterministic(networkParameters, Script.ScriptType.P2PKH);
//            BitcoinWallet bitcoinWallet = buildBitcoinWallet(wallet);
//            // Save the wallet details in the database
//            return bitcoinWalletRepository.save(bitcoinWallet);
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to create Bitcoin wallet", e);
//        }
//    }
//
//    private BitcoinWallet buildBitcoinWallet(Wallet wallet) {
//        DeterministicSeed keyChainSeed = wallet.getKeyChainSeed();
//        // Convert the mnemonic code to a space-separated string
//        String seedWords = Joiner.on(" ").join(Objects.requireNonNull(keyChainSeed.getMnemonicCode()));
//
//        // Get the current receiving address and associated keys
//        Address address = wallet.currentReceiveAddress();
//        ECKey keyFromAddress = wallet.findKeyFromAddress(address);
//
//        // Convert keys and address to strings for storage
//        String bitcoinAddress = address.toString();
//        String privateKeyAsHex = keyFromAddress.getPrivateKeyAsHex();
//        String publicKeyAsHex = keyFromAddress.getPublicKeyAsHex();
//
//        // Construct and return the BitcoinWallet object
//        return BitcoinWallet.builder()
//                .address(bitcoinAddress)
//                .balance(wallet.getBalance().getValue())
//                .privateKey(privateKeyAsHex)
//                .publicKey(publicKeyAsHex)
//                .seedWords(seedWords)
//                .createdAt(convertEpochToDate(wallet.getEarliestKeyCreationTime()))
//                .updatedAt(convertEpochToDate(wallet.getLastBlockSeenTimeSecs()))
//                .build();
//    }
//
//    private static Date convertEpochToDate(long epochSeconds) {
//        // Convert epoch seconds to Date
//        Instant instant = Instant.ofEpochSecond(epochSeconds);
//        return Date.from(instant);
//    }
//}
