package com.fsdm.test.bitcoinbj.model.resource;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionResource {
    private String transactionId;
    private List<TransactionInputResource> inputs;
    private List<TransactionOutputResource> outputs;
}
