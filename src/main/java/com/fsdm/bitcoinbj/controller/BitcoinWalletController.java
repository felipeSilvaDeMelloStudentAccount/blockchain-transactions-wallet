package com.fsdm.bitcoinbj.controller;

import com.fsdm.bitcoinbj.model.wallet.BitcoinWallet;
import com.fsdm.bitcoinbj.service.BitcoinWalletService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wallets")
@AllArgsConstructor
public class BitcoinWalletController {
    private final BitcoinWalletService bitcoinWalletService;

    /**
     * Create a new wallet
     *
     * @return Wallet   Wallet object
     */
    @PostMapping
    public ResponseEntity<?> createWallet() {
        try {
            BitcoinWallet bitcoinWallet = bitcoinWalletService.createWallet();
            return ResponseEntity.ok(bitcoinWallet);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create wallet: " + e.getMessage());
        }
    }
}
