package com.fsdm.bitcoinbj.controller;

import com.fsdm.bitcoinbj.model.resource.BlockResource;
import com.fsdm.bitcoinbj.model.resource.TransactionResource;
import com.fsdm.bitcoinbj.model.transaction.BlockDAO;
import com.fsdm.bitcoinbj.service.BlockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/blocks")
@AllArgsConstructor
@Tag(name = "Blocks", description = "Block management APIs")
public class BlockController {

    private final BlockService blockService;

    @GetMapping
    @Operation(summary = "Get all blocks", description = "Retrieve a list of all blocks with pagination")
    public Page<BlockDAO> getAllBlocks(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return blockService.getAllBlocks(pageable);
    }

    @GetMapping("/{hash}")
    @Operation(summary = "Get block by hash", description = "Retrieve a block by its hash")
    public EntityModel<BlockResource> getBlockByHash(@PathVariable String hash) {
        return blockService.getBlockResourceByHash(hash);
    }

    @GetMapping("/{hash}/transactions")
    @Operation(summary = "Get transactions by block hash", description = "Retrieve transactions for a given block hash")
    public List<TransactionResource> getTransactionsByBlockHash(@PathVariable String hash) {
        return blockService.getTransactionsByBlockHash(hash);
    }
}
