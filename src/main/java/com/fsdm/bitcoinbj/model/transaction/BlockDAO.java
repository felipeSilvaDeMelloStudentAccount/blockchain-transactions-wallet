package com.fsdm.bitcoinbj.model.transaction;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "blocks")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BlockDAO {

  @Id
  private String hash;
  private String previousHash;
  private long nonce;
  private long difficulty;
  private Instant timestamp;

  @OneToMany(mappedBy = "blockDAO", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JsonManagedReference
  private List<TransactionDAO> transactions;
}
