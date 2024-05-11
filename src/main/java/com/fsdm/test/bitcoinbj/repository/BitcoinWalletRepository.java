package com.fsdm.test.bitcoinbj.repository;

import com.fsdm.test.bitcoinbj.model.BitcoinWallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BitcoinWalletRepository extends JpaRepository<BitcoinWallet, Long> {

    BitcoinWallet findByAddress(String address);
}
