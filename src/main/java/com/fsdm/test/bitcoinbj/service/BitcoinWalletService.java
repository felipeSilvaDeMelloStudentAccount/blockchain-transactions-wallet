package com.fsdm.test.bitcoinbj.service;

import com.fsdm.test.bitcoinbj.model.wallet.BitcoinWallet;
import com.fsdm.test.bitcoinbj.repository.BitcoinWalletRepository;
import lombok.extern.slf4j.Slf4j;
import org.bitcoinj.core.Address;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.script.Script;
import org.bitcoinj.wallet.Wallet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;

@Service
@Slf4j
public class BitcoinWalletService {

    private final NetworkParameters networkParameters;
    private final BitcoinWalletRepository bitcoinWalletRepository;

    public BitcoinWalletService(@Value("${bitcoin.network}") String network, BitcoinWalletRepository bitcoinWalletRepository) {
        this.networkParameters = NetworkParameters.fromID(network);
        if (this.networkParameters == null) {
            throw new IllegalArgumentException("Unsupported Bitcoin network ID: " + network);
        }
        this.bitcoinWalletRepository = bitcoinWalletRepository;
    }

    public BitcoinWallet createWallet() {
        Wallet wallet = Wallet.createDeterministic(networkParameters, Script.ScriptType.P2PKH);
        BitcoinWallet bitcoinWallet = buildBitcoinWallet(wallet);
        return bitcoinWalletRepository.save(bitcoinWallet);
    }

    private BitcoinWallet buildBitcoinWallet(Wallet wallet) {
        String seedWords = String.join(" ", wallet.getKeyChainSeed().getMnemonicCode());
        Address address = wallet.currentReceiveAddress();
        ECKey key = wallet.findKeyFromAddress(address);

        return BitcoinWallet.builder()
                .address(address.toString())
                .balance(wallet.getBalance().getValue())
                .privateKey(key.getPrivateKeyAsHex())
                .publicKey(key.getPublicKeyAsHex())
                .seedWords(seedWords)
                .createdAt(Date.from(Instant.ofEpochSecond(wallet.getEarliestKeyCreationTime())))
                .updatedAt(Date.from(Instant.now()))
                .build();
    }
}

