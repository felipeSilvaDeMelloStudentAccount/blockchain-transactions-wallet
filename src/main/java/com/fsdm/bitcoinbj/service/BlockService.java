package com.fsdm.bitcoinbj.service;

import com.fsdm.bitcoinbj.model.resource.TransactionResource;
import com.fsdm.bitcoinbj.model.resource.BlockResource;
import com.fsdm.bitcoinbj.model.transaction.BlockDAO;
import org.springframework.hateoas.EntityModel;

import java.util.List;

public interface BlockService {
    List<BlockDAO> getAllBlocks();

    BlockDAO getBlockByHash(String hash);

    EntityModel<BlockResource> getBlockResourceByHash(String hash);

    List<TransactionResource> getTransactionsByBlockHash(String hash);
}


