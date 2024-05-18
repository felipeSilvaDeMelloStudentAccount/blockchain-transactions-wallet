package com.fsdm.bitcoinbj.service;

import com.fsdm.bitcoinbj.model.resource.BlockResource;
import com.fsdm.bitcoinbj.model.resource.TransactionResource;
import com.fsdm.bitcoinbj.model.transaction.BlockDAO;
import org.bitcoinj.core.Block;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;

import java.util.List;

public interface BlockService {

    EntityModel<BlockResource> getBlockResourceByHash(String hash);

    List<TransactionResource> getTransactionsByBlockHash(String hash);

    Page<BlockDAO> getAllBlocks(Pageable pageable);

    void saveBlock(Block block);
}
