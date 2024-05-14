package com.fsdm.bitcoinbj.model.wallet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BitcoinWalletDAO {
    private Long id;
    private Date createdAt;
    private String peerInfo;
    private int transactions; // Number of transactions in the block
    private long nonce; // Nonce of the block
    private long difficulty; // Difficulty of the block
    private List<String> transactionDetails; // Details of each transaction in a block
}
