package com.fsdm.bitcoinbj.controller;

import com.fsdm.bitcoinbj.model.resource.BlockResource;
import com.fsdm.bitcoinbj.model.resource.TransactionResource;
import com.fsdm.bitcoinbj.model.transaction.BlockDAO;
import com.fsdm.bitcoinbj.service.BlockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing Bitcoin blocks.
 * <p>
 * This controller provides endpoints for retrieving block data and transactions within blocks.
 * </p>
 */
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/blocks")
@AllArgsConstructor
@Tag(name = "Blocks", description = "Block management APIs")
public class BlockController {

  private final BlockService blockService;

  /**
   * Endpoint for retrieving a paginated list of all blocks.
   * <p>
   * This method calls the {@link BlockService#getAllBlocks(Pageable)} method to get the blocks.
   * </p>
   *
   * @param page the page number to retrieve
   * @param size the number of blocks per page
   * @return a paginated list of blocks
   */
  @GetMapping
  @Operation(summary = "Get all blocks", description = "Retrieve a list of all blocks with pagination")
  public Page<BlockDAO> getAllBlocks(@RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "20") int size) {
    Pageable pageable = PageRequest.of(page, size);
    return blockService.getAllBlocks(pageable);
  }

  /**
   * Endpoint for retrieving a block by its hash.
   * <p>
   * This method calls the {@link BlockService#getBlockResourceByHash(String)} method to get the
   * block details.
   * </p>
   *
   * @param hash the hash of the block to retrieve
   * @return the block details wrapped in an {@link EntityModel}
   */
  @GetMapping("/{hash}")
  @Operation(summary = "Get block by hash", description = "Retrieve a block by its hash")
  public EntityModel<BlockResource> getBlockByHash(@PathVariable String hash) {
    return blockService.getBlockResourceByHash(hash);
  }

  /**
   * Endpoint for retrieving transactions of a specific block by its hash.
   * <p>
   * This method calls the {@link BlockService#getTransactionsByBlockHash(String)} method to get the
   * transactions.
   * </p>
   *
   * @param hash the hash of the block to retrieve transactions for
   * @return a list of transactions for the specified block
   */
  @GetMapping("/{hash}/transactions")
  @Operation(summary = "Get transactions by block hash", description = "Retrieve transactions for a given block hash")
  public List<TransactionResource> getTransactionsByBlockHash(@PathVariable String hash) {
    return blockService.getTransactionsByBlockHash(hash);
  }
}
