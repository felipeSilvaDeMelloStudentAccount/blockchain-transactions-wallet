package com.fsdm.test.bitcoinbj.model.transaction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlockDTO {
    private String hash;
    private String previousHash;
    private String nextHash;
    private String lastHash;
}
