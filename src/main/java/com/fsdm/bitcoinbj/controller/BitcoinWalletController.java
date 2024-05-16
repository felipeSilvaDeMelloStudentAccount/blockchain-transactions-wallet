package com.fsdm.bitcoinbj.controller;

import com.fsdm.bitcoinbj.model.wallet.BitcoinWallet;
import com.fsdm.bitcoinbj.service.BitcoinWalletService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing Bitcoin wallets.
 * <p>
 * This controller provides endpoints for creating new Bitcoin wallets.
 * </p>
 */
@RestController
@RequestMapping("/wallets")
@AllArgsConstructor
@Tag(name = "Wallets", description = "Bitcoin wallet management APIs")
public class BitcoinWalletController {
    private final BitcoinWalletService bitcoinWalletService;

    /**
     * Endpoint for creating a new Bitcoin wallet.
     * <p>
     * This method calls the {@link BitcoinWalletService#createWallet()} method to create a new wallet.
     * </p>
     *
     * @return a {@link ResponseEntity} containing the created {@link BitcoinWallet} or an error message
     */
    @PostMapping
    @Operation(summary = "Create wallet", description = "Create a new Bitcoin wallet")
    public ResponseEntity<?> createWallet() {
        try {
            BitcoinWallet bitcoinWallet = bitcoinWalletService.createWallet();
            return ResponseEntity.ok(bitcoinWallet);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create wallet: " + e.getMessage());
        }
    }
}


