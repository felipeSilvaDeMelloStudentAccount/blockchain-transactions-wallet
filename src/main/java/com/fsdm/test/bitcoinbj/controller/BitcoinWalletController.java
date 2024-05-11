package com.fsdm.test.bitcoinbj.controller;

import com.fsdm.test.bitcoinbj.model.BitcoinWallet;
import com.fsdm.test.bitcoinbj.service.BitcoinWalletService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bitcoin/wallets")
@AllArgsConstructor
public class BitcoinWalletController {
    private BitcoinWalletService bitcoinWalletService;

    @PostMapping()
    public BitcoinWallet createWallet() {
        return bitcoinWalletService.createWallet();
    }
}
