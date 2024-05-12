package com.fsdm.test.bitcoinbj.repository;

import com.fsdm.test.bitcoinbj.model.BlockDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlockRepository extends JpaRepository<BlockDAO, String> {
}
