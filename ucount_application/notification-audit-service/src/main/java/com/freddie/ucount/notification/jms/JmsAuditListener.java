package com.freddie.ucount.notification.jms;

import com.freddie.ucount.notification.entity.AuditLogMessage;
import com.freddie.ucount.notification.repository.AuditLogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class JmsAuditListener {

    private static final Logger log = LoggerFactory.getLogger(JmsAuditListener.class);

    private final AuditLogRepository auditLogRepository;

    public JmsAuditListener(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @JmsListener(destination = "ucount.audit.queue")
    public void processAuditMessage(String textMessage) {
        log.info("Received ActiveMQ JMS Audit Message: {}", textMessage);

        try {
            String correlationId = "CORR-" + UUID.randomUUID().toString().substring(0, 8);
            String[] parts = textMessage.split("\\|", 2);
            String source = parts.length > 0 ? parts[0] : "UNKNOWN";
            String payload = parts.length > 1 ? parts[1] : textMessage;

            AuditLogMessage audit = new AuditLogMessage(correlationId, source, payload);
            auditLogRepository.save(audit);
        } catch (Exception e) {
            log.error("Error processing ActiveMQ JMS audit log. Routing to Dead Letter Topic (DLT): {}", e.getMessage());
        }
    }
}
