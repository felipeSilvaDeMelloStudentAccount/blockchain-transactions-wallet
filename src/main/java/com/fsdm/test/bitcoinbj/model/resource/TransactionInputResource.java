package com.fsdm.test.bitcoinbj.model.resource;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionInputResource {
    private Long id;
    private String sourceTransactionId;
    private int outputIndex;
    private String scriptSig;
}