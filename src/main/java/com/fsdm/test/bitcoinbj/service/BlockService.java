package com.fsdm.test.bitcoinbj.service;

import com.fsdm.test.bitcoinbj.model.transaction.BlockDAO;
import com.fsdm.test.bitcoinbj.model.transaction.BlockDTO;

import java.util.List;

public interface BlockService {
    List<BlockDAO> getAllBlocks();

    BlockDAO getBlockByHash(String hash);

    BlockDTO getFormattedBlockByHash(String hash);
}
