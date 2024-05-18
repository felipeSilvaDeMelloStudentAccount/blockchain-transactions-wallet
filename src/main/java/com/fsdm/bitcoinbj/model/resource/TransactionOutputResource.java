package com.fsdm.bitcoinbj.model.resource;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionOutputResource {

  private Long id;
  private BigDecimal value;
  private String scriptPubKey;
}
