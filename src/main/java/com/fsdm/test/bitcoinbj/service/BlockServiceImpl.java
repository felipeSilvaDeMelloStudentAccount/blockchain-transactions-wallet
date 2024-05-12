package com.fsdm.test.bitcoinbj.service;

import com.fsdm.test.bitcoinbj.model.BlockDAO;
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
        // Use Optional to safely handle null cases
        Optional<BlockDAO> block = blockRepository.findById(hash);
        return block.orElse(null);  // Return null if the block is not found
    }
}
