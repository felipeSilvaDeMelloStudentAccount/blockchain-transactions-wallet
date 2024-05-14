package com.fsdm.bitcoinbj.controller;

import com.fsdm.bitcoinbj.model.resource.TransactionResource;
import com.fsdm.bitcoinbj.service.BlockService;
import com.fsdm.bitcoinbj.model.resource.BlockResource;
import com.fsdm.bitcoinbj.model.transaction.BlockDAO;
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


    /**
     * Get all blocks
     *
     * @return List of all blocks
     */
    @GetMapping
    public List<BlockDAO> getAllBlocks() {
        return blockService.getAllBlocks();
    }

    /**
     * Get block by hash
     *
     * @param hash Block hash
     * @return Block resource
     */
    @GetMapping("/{hash}")
    public EntityModel<BlockResource> getBlockByHash(@PathVariable String hash) {
        return blockService.getBlockResourceByHash(hash);
    }

    /**
     * Get transactions by block hash
     *
     * @param hash Block hash
     * @return List of transactions
     */
    @GetMapping("/{hash}/transactions")
    public List<TransactionResource> getTransactionsByBlockHash(@PathVariable String hash) {
        return blockService.getTransactionsByBlockHash(hash);
    }
}




