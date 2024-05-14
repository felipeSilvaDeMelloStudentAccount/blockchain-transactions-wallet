package com.fsdm.test.bitcoinbj.model.resource;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionOutputResource {
    private Long id;
    private BigDecimal value;
    private String scriptPubKey;
}
