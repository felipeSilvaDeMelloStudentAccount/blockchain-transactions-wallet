package com.fsdm.bitcoinbj.model.transaction;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "blocks")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BlockDAO {
    @Id
    private String hash;
    private String previousHash;
    private long nonce;
    private long difficulty;
    private Instant timestamp;

    @OneToMany(mappedBy = "blockDAO", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TransactionDAO> transactions;
}

