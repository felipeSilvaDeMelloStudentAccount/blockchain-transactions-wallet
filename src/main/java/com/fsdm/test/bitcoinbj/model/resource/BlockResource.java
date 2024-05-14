package com.fsdm.test.bitcoinbj.model.resource;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.time.Instant;
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
    private Instant timestamp;
    private List<TransactionResource> transactions;
}

