package com.fsdm.test.bitcoinbj.controller;

import com.fsdm.test.bitcoinbj.model.BlockDAO;
import com.fsdm.test.bitcoinbj.service.BlockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/blocks")
public class BlockController {

    @Autowired
    private BlockService blockService;

    @GetMapping
    public List<BlockDAO> getAllBlocks() {
        return blockService.getAllBlocks();
    }

    @GetMapping("/{hash}")
    public BlockDAO getBlockByHash(@PathVariable String hash) {
        return blockService.getBlockByHash(hash);
    }
}
