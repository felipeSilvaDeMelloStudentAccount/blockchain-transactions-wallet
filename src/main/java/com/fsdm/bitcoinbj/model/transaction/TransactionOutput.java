package com.fsdm.bitcoinbj.model.transaction;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "transaction_outputs")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionOutput {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BigDecimal value;
    private String scriptPubKey;

    @ManyToOne
    @JoinColumn(name = "transaction_id")
    @JsonBackReference
    private TransactionDAO transactionDAO;
}

