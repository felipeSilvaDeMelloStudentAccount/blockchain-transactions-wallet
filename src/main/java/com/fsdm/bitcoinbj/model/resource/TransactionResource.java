package com.fsdm.bitcoinbj.model.resource;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionResource {

  private String transactionId;
  private List<TransactionInputResource> inputs;
  private List<TransactionOutputResource> outputs;
}
