package com.fsdm.bitcoinbj.model.resource;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlockResource extends RepresentationModel<BlockResource> {
    private String hash;
    private String previousHash;
    private String nextHash;
    private String lastHash;
    private long nonce;
    private long difficulty;
    private String timestamp;  // Updated to String
    private List<TransactionResource> transactions;
}


