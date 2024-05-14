package com.fsdm.bitcoinbj.model.transaction;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDAO {
    @Id
    private String transactionId;

    @ManyToOne
    @JoinColumn(name = "block_hash")
    private BlockDAO blockDAO;

    @OneToMany(mappedBy = "transactionDAO", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TransactionInput> inputs;

    @OneToMany(mappedBy = "transactionDAO", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TransactionOutput> outputs;
}

