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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

/**
 * This service provides methods to manage and retrieve blockchain data, such as blocks and transactions, from the database.
 * It also maps database entities to resource representations that can be used in the API responses.
 */
@Service
public class BlockServiceImpl implements BlockService {

    @Autowired
    private BlockRepository blockRepository;

    /**
     * Retrieves a paginated list of all blocks.
     *
     * @param pageable the pagination information
     * @return a paginated list of BlockDAO objects
     */
    @Override
    public Page<BlockDAO> getAllBlocks(Pageable pageable) {
        return blockRepository.findAll(pageable);
    }

    /**
     * Retrieves a block resource by its hash.
     *
     * @param hash the hash of the block
     * @return an EntityModel containing the BlockResource, or null if the block is not found
     */
    @Override
    @Transactional
    public EntityModel<BlockResource> getBlockResourceByHash(String hash) {
        Optional<BlockDAO> blockOptional = blockRepository.findById(hash);
        if (!blockOptional.isPresent()) {
            return null;
        }

        BlockDAO block = blockOptional.get();

        String nextHash = getNextHash(block);
        String lastHash = getLastHash();

        List<TransactionResource> transactionResources = block.getTransactions().stream().map(this::mapToTransactionResource).collect(Collectors.toList());

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

    /**
     * Retrieves a list of transactions for a given block hash.
     *
     * @param hash the hash of the block
     * @return a list of TransactionResource objects, or an empty list if the block is not found
     */
    @Override
    @Transactional
    public List<TransactionResource> getTransactionsByBlockHash(String hash) {
        Optional<BlockDAO> blockOptional = blockRepository.findById(hash);
        if (!blockOptional.isPresent()) {
            return Collections.emptyList();
        }

        BlockDAO block = blockOptional.get();
        block.getTransactions().size(); // Ensure transactions are loaded

        return block.getTransactions().stream()
                .map(this::mapToTransactionResource)
                .collect(Collectors.toList());
    }

    /**
     * Maps a TransactionDAO to a TransactionResource.
     *
     * @param tx the TransactionDAO to map
     * @return the mapped TransactionResource
     */
    private TransactionResource mapToTransactionResource(TransactionDAO tx) {
        List<TransactionInputResource> inputResources = tx.getInputs().stream()
                .map(this::mapToInputResource)
                .collect(Collectors.toList());

        List<TransactionOutputResource> outputResources = tx.getOutputs().stream()
                .map(this::mapToOutputResource)
                .collect(Collectors.toList());

        return new TransactionResource(tx.getTransactionId(), inputResources, outputResources);
    }

    /**
     * Maps a TransactionInput to a TransactionInputResource.
     *
     * @param input the TransactionInput to map
     * @return the mapped TransactionInputResource
     */
    private TransactionInputResource mapToInputResource(TransactionInput input) {
        return new TransactionInputResource(
                input.getId(),
                input.getSourceTransactionId(),
                input.getOutputIndex(),
                input.getScriptSig()
        );
    }

    /**
     * Maps a TransactionOutput to a TransactionOutputResource.
     *
     * @param output the TransactionOutput to map
     * @return the mapped TransactionOutputResource
     */
    private TransactionOutputResource mapToOutputResource(TransactionOutput output) {
        return new TransactionOutputResource(
                output.getId(),
                output.getValue(),
                output.getScriptPubKey()
        );
    }

    /**
     * Retrieves the hash of the next block in the blockchain.
     *
     * @param block the current block
     * @return the hash of the next block, or null if not found
     */
    private String getNextHash(BlockDAO block) {
        Optional<BlockDAO> nextBlock = blockRepository.findAll().stream()
                .filter(b -> b.getPreviousHash().equals(block.getHash()))
                .findFirst();

        return nextBlock.map(BlockDAO::getHash).orElse(null);
    }

    /**
     * Retrieves the hash of the last block in the blockchain.
     *
     * @return the hash of the last block, or null if not found
     */
    private String getLastHash() {
        return blockRepository.findAll().stream()
                .reduce((first, second) -> second)
                .map(BlockDAO::getHash)
                .orElse(null);
    }
}
