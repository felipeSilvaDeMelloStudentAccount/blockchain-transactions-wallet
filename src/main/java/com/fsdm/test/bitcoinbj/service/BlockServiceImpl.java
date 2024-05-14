package com.fsdm.test.bitcoinbj.service;

import com.fsdm.test.bitcoinbj.model.transaction.BlockDAO;
import com.fsdm.test.bitcoinbj.model.transaction.BlockDTO;
import com.fsdm.test.bitcoinbj.repository.BlockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BlockServiceImpl implements BlockService {

    @Autowired
    private BlockRepository blockRepository;

    @Override
    public List<BlockDAO> getAllBlocks() {
        return blockRepository.findAll();
    }

    @Override
    public BlockDAO getBlockByHash(String hash) {
        Optional<BlockDAO> block = blockRepository.findById(hash);
        return block.orElse(null);
    }

    @Override
    public BlockDTO getFormattedBlockByHash(String hash) {
        Optional<BlockDAO> blockOptional = blockRepository.findById(hash);
        if (!blockOptional.isPresent()) {
            return null;
        }

        BlockDAO block = blockOptional.get();
        String nextHash = getNextHash(block);
        String lastHash = getLastHash();

        return new BlockDTO(
                block.getHash(),
                block.getPreviousHash(),
                nextHash,
                lastHash
        );
    }

    private String getNextHash(BlockDAO block) {
        // Logic to get the next block hash (if exists)
        // This might involve querying the database for a block with this block's hash as the previousHash
        Optional<BlockDAO> nextBlock = blockRepository.findAll().stream()
                .filter(b -> b.getPreviousHash().equals(block.getHash()))
                .findFirst();

        return nextBlock.map(BlockDAO::getHash).orElse(null);
    }

    private String getLastHash() {
        // Logic to get the last block hash
        // This might involve querying the database for the block with the highest block height or timestamp
        return blockRepository.findAll().stream()
                .reduce((first, second) -> second) // Get the last element in the list
                .map(BlockDAO::getHash)
                .orElse(null);
    }
}

