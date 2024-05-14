package com.fsdm.test.bitcoinbj.controller;

import com.fsdm.test.bitcoinbj.model.resource.BlockResource;
import com.fsdm.test.bitcoinbj.model.resource.TransactionResource;
import com.fsdm.test.bitcoinbj.model.transaction.BlockDAO;
import com.fsdm.test.bitcoinbj.service.BlockService;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/blocks")
@AllArgsConstructor
public class BlockController {

    private final BlockService blockService;

    @GetMapping
    public List<BlockDAO> getAllBlocks() {
        return blockService.getAllBlocks();
    }

    @GetMapping("/{hash}")
    public EntityModel<BlockResource> getBlockByHash(@PathVariable String hash) {
        return blockService.getBlockResourceByHash(hash);
    }

    @GetMapping("/{hash}/transactions")
    public List<TransactionResource> getTransactionsByBlockHash(@PathVariable String hash) {
        return blockService.getTransactionsByBlockHash(hash);
    }
}




