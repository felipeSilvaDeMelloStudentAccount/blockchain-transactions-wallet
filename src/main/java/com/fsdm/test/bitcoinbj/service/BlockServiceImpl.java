package com.fsdm.test.bitcoinbj.service;

import com.fsdm.test.bitcoinbj.controller.BlockController;
import com.fsdm.test.bitcoinbj.model.resource.BlockResource;
import com.fsdm.test.bitcoinbj.model.resource.TransactionInputResource;
import com.fsdm.test.bitcoinbj.model.resource.TransactionOutputResource;
import com.fsdm.test.bitcoinbj.model.resource.TransactionResource;
import com.fsdm.test.bitcoinbj.model.transaction.BlockDAO;
import com.fsdm.test.bitcoinbj.model.transaction.TransactionDAO;
import com.fsdm.test.bitcoinbj.model.transaction.TransactionInput;
import com.fsdm.test.bitcoinbj.model.transaction.TransactionOutput;
import com.fsdm.test.bitcoinbj.repository.BlockRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class BlockServiceImpl implements BlockService {

    private static final Logger log = LoggerFactory.getLogger(BlockServiceImpl.class);

    @Autowired
    private BlockRepository blockRepository;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy 'at' HH:mm:ss").withZone(ZoneId.systemDefault());

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
    public EntityModel<BlockResource> getBlockResourceByHash(String hash) {
        Optional<BlockDAO> blockOptional = blockRepository.findById(hash);
        if (!blockOptional.isPresent()) {
            return null;
        }

        BlockDAO block = blockOptional.get();
        // Ensure transactions are loaded
        block.getTransactions().forEach(tx -> {
            tx.getInputs().size();
            tx.getOutputs().size();
        });

        String nextHash = getNextHash(block);
        String lastHash = getLastHash();

        List<TransactionResource> transactionResources = block.getTransactions().stream()
                .map(this::mapToTransactionResource)
                .collect(Collectors.toList());

        // Format the timestamp to a human-readable string
        String formattedTimestamp = block.getTimestamp().atZone(ZoneId.systemDefault()).format(formatter);

        BlockResource blockResource = new BlockResource(
                block.getHash(),
                block.getPreviousHash(),
                nextHash,
                lastHash,
                block.getNonce(),
                block.getDifficulty(),
                formattedTimestamp,
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
    public List<TransactionResource> getTransactionsByBlockHash(String hash) {
        Optional<BlockDAO> blockOptional = blockRepository.findById(hash);
        if (!blockOptional.isPresent()) {
            return null;
        }

        BlockDAO block = blockOptional.get();
        // Ensure transactions are loaded
        block.getTransactions().forEach(tx -> {
            tx.getInputs().size();
            tx.getOutputs().size();
        });

        return block.getTransactions().stream()
                .map(this::mapToTransactionResource)
                .collect(Collectors.toList());
    }

    private TransactionResource mapToTransactionResource(TransactionDAO tx) {
        // Ensure inputs and outputs are loaded
        tx.getInputs().size();
        tx.getOutputs().size();

        log.info("Mapping transaction: {}", tx.getTransactionId());
        tx.getInputs().forEach(input -> log.info("Input: {}", input));
        tx.getOutputs().forEach(output -> log.info("Output: {}", output));

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
