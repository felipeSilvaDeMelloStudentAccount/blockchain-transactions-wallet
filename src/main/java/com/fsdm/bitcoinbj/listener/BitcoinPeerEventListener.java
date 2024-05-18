package com.fsdm.bitcoinbj.listener;

import com.fsdm.bitcoinbj.service.BlockService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bitcoinj.core.Block;
import org.bitcoinj.core.FilteredBlock;
import org.bitcoinj.core.Peer;
import org.bitcoinj.core.listeners.BlocksDownloadedEventListener;
import org.bitcoinj.core.listeners.PeerConnectedEventListener;
import org.bitcoinj.core.listeners.PeerDisconnectedEventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Class Overview: This class listens for events from Bitcoin peers, such as when peers connect,
 * disconnect, and when blocks are downloaded. It processes the downloaded blocks and transactions,
 * and saves them to a PostgreSQL database. implements PeerConnectedEventListener,
 * PeerDisconnectedEventListener, and BlocksDownloadedEventListener to respond to Bitcoin network
 * events.
 */
@Component
@Slf4j
@AllArgsConstructor
public class BitcoinPeerEventListener implements PeerConnectedEventListener,
    PeerDisconnectedEventListener, BlocksDownloadedEventListener {

  private final BlockService blockService;

  /**
   * Handles the event when a peer is connected.
   *
   * @param peer      the connected peer
   * @param peerCount the total number of connected peers
   */
  @Override
  public void onPeerConnected(Peer peer, int peerCount) {
    log.info("Connected to peer: {}", peer);
  }

  /**
   * Handles the event when a peer is disconnected.
   *
   * @param peer      the disconnected peer
   * @param peerCount the total number of connected peers
   */
  @Override
  public void onPeerDisconnected(Peer peer, int peerCount) {
    log.info("Disconnected from peer: {}", peer);
  }


  /**
   * Handles the event when blocks are downloaded from a peer. This method is annotated with
   *
   * @param peer          the peer from which the block was downloaded
   * @param block         the downloaded block
   * @param filteredBlock the filtered block
   * @param blocksLeft    the number of blocks left to download
   * @Transactional to ensure that the database operations are performed within a transaction
   * context.
   */
  @Override
  @Transactional
  public void onBlocksDownloaded(Peer peer, Block block, FilteredBlock filteredBlock,
      int blocksLeft) {
    log.info("Block downloaded: {} from peer: {}", block.getHashAsString(), peer);
    log.info("Block details: Nonce: {}, Difficulty: {}, Transactions: {}", block.getNonce(),
        block.getDifficultyTarget(), block.getTransactions().size());
    blockService.saveBlock(block);
  }
}
