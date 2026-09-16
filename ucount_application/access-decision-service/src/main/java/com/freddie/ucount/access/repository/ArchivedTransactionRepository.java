package com.freddie.ucount.access.repository;

import com.freddie.ucount.access.entity.ArchivedTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArchivedTransactionRepository extends JpaRepository<ArchivedTransaction, Long> {

    List<ArchivedTransaction> findByUserId(String userId);

    List<ArchivedTransaction> findByOriginalProfileId(String originalProfileId);
}
