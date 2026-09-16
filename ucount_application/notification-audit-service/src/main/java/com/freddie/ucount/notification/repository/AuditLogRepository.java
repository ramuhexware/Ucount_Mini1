package com.freddie.ucount.notification.repository;

import com.freddie.ucount.notification.entity.AuditLogMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLogMessage, Long> {

    List<AuditLogMessage> findByCorrelationId(String correlationId);
}
