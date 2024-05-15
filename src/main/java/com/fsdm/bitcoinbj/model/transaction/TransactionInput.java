package com.fsdm.bitcoinbj.model.transaction;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "transaction_inputs")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionInput {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String sourceTransactionId;
    private int outputIndex;
    private String scriptSig;

    @ManyToOne
    @JoinColumn(name = "transaction_id")
    @JsonBackReference
    private TransactionDAO transactionDAO;
}

