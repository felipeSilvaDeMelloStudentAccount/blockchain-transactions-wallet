package com.fsdm.bitcoinbj.repository;


import com.fsdm.bitcoinbj.model.transaction.BlockDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlockRepository extends JpaRepository<BlockDAO, String> {

}

