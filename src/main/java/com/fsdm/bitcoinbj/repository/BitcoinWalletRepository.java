package com.fsdm.bitcoinbj.repository;

import com.fsdm.bitcoinbj.model.wallet.BitcoinWallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BitcoinWalletRepository extends JpaRepository<BitcoinWallet, Long> {
    BitcoinWallet findByAddress(String address);
}
