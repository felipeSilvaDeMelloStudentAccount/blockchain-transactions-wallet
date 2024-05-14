package com.fsdm.test.bitcoinbj.service;

import com.fsdm.test.bitcoinbj.model.resource.BlockResource;
import com.fsdm.test.bitcoinbj.model.resource.TransactionResource;
import com.fsdm.test.bitcoinbj.model.transaction.BlockDAO;
import org.springframework.hateoas.EntityModel;

import java.util.List;

public interface BlockService {
    List<BlockDAO> getAllBlocks();

    BlockDAO getBlockByHash(String hash);

    EntityModel<BlockResource> getBlockResourceByHash(String hash);

    List<TransactionResource> getTransactionsByBlockHash(String hash);
}


