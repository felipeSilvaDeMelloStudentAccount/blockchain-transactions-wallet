package com.fsdm.bitcoinbj.repository;

import com.fsdm.bitcoinbj.model.transaction.BlockDAO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class BlockRepositoryTest {

    @Autowired
    private BlockRepository blockRepository;

    @Test
    public void testSaveBlock() {
        BlockDAO blockDAO = new BlockDAO();
        blockDAO.setHash("testhash");
        blockDAO.setPreviousHash("previoushash");
        blockDAO.setNonce(12345);
        blockDAO.setDifficulty(1);
        blockDAO.setTimestamp(Instant.now());
        blockDAO.setTransactions(new ArrayList<>());

        blockRepository.save(blockDAO);

        Optional<BlockDAO> retrievedBlock = blockRepository.findById("testhash");
        assertTrue(retrievedBlock.isPresent());
    }
}
