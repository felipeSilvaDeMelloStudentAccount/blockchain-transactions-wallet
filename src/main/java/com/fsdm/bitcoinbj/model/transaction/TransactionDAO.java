package com.fsdm.bitcoinbj.model.transaction;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    @JsonBackReference
    private BlockDAO blockDAO;

    @OneToMany(mappedBy = "transactionDAO", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<TransactionInput> inputs;

    @OneToMany(mappedBy = "transactionDAO", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<TransactionOutput> outputs;
}