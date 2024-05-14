package com.fsdm.bitcoinbj.service;

import com.fsdm.bitcoinbj.controller.BlockController;
import com.fsdm.bitcoinbj.model.resource.BlockResource;
import com.fsdm.bitcoinbj.model.resource.TransactionInputResource;
import com.fsdm.bitcoinbj.model.resource.TransactionOutputResource;
import com.fsdm.bitcoinbj.model.resource.TransactionResource;
import com.fsdm.bitcoinbj.model.transaction.BlockDAO;
import com.fsdm.bitcoinbj.model.transaction.TransactionDAO;
import com.fsdm.bitcoinbj.model.transaction.TransactionInput;
import com.fsdm.bitcoinbj.model.transaction.TransactionOutput;
import com.fsdm.bitcoinbj.repository.BlockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

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
    @Transactional
    public EntityModel<BlockResource> getBlockResourceByHash(String hash) {
        Optional<BlockDAO> blockOptional = blockRepository.findById(hash);
        if (!blockOptional.isPresent()) {
            return null;
        }

        BlockDAO block = blockOptional.get();
        block.getTransactions().size(); // Ensure transactions are loaded

        String nextHash = getNextHash(block);
        String lastHash = getLastHash();

        List<TransactionResource> transactionResources = block.getTransactions().stream()
                .map(this::mapToTransactionResource)
                .collect(Collectors.toList());

        BlockResource blockResource = new BlockResource(
                block.getHash(),
                block.getPreviousHash(),
                nextHash,
                lastHash,
                block.getNonce(),
                block.getDifficulty(),
                block.getTimestamp().toString(),  // Ensure proper format
                transactionResources
        );

        // Add HATEOAS links
        EntityModel<BlockResource> resource = EntityModel.of(blockResource);
        resource.add(linkTo(methodOn(BlockController.class).getBlockByHash(block.getHash())).withSelfRel());
        if (block.getPreviousHash() != null) {
            resource.add(linkTo(methodOn(BlockController.class).getBlockByHash(block.getPreviousHash())).withRel("previousHash"));
        }
        if (nextHash != null) {
            resource.add(linkTo(methodOn(BlockController.class).getBlockByHash(nextHash)).withRel("nextHash"));
        }
        if (lastHash != null) {
            resource.add(linkTo(methodOn(BlockController.class).getBlockByHash(lastHash)).withRel("lastHash"));
        }

        return resource;
    }

    @Override
    @Transactional
    public List<TransactionResource> getTransactionsByBlockHash(String hash) {
        Optional<BlockDAO> blockOptional = blockRepository.findById(hash);
        if (!blockOptional.isPresent()) {
            return null;
        }

        BlockDAO block = blockOptional.get();
        block.getTransactions().size(); // Ensure transactions are loaded

        return block.getTransactions().stream()
                .map(this::mapToTransactionResource)
                .collect(Collectors.toList());
    }

    private TransactionResource mapToTransactionResource(TransactionDAO tx) {
        tx.getInputs().size();  // Ensure inputs are loaded
        tx.getOutputs().size(); // Ensure outputs are loaded

        List<TransactionInputResource> inputResources = tx.getInputs().stream()
                .map(this::mapToInputResource)
                .collect(Collectors.toList());

        List<TransactionOutputResource> outputResources = tx.getOutputs().stream()
                .map(this::mapToOutputResource)
                .collect(Collectors.toList());

        return new TransactionResource(tx.getTransactionId(), inputResources, outputResources);
    }

    private TransactionInputResource mapToInputResource(TransactionInput input) {
        return new TransactionInputResource(
                input.getId(),
                input.getSourceTransactionId(),
                input.getOutputIndex(),
                input.getScriptSig()
        );
    }

    private TransactionOutputResource mapToOutputResource(TransactionOutput output) {
        return new TransactionOutputResource(
                output.getId(),
                output.getValue(),
                output.getScriptPubKey()
        );
    }

    private String getNextHash(BlockDAO block) {
        Optional<BlockDAO> nextBlock = blockRepository.findAll().stream()
                .filter(b -> b.getPreviousHash().equals(block.getHash()))
                .findFirst();

        return nextBlock.map(BlockDAO::getHash).orElse(null);
    }

    private String getLastHash() {
        return blockRepository.findAll().stream()
                .reduce((first, second) -> second)
                .map(BlockDAO::getHash)
                .orElse(null);
    }
}
