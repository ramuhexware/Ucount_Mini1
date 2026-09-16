package com.freddie.ucount.notification.service;

import com.freddie.ucount.notification.entity.AuditLogMessage;
import com.freddie.ucount.notification.entity.CommonComment;
import com.freddie.ucount.notification.repository.AuditLogRepository;
import com.freddie.ucount.notification.repository.CommonCommentRepository;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CommonCommentService {

    private final CommonCommentRepository commentRepository;
    private final AuditLogRepository auditLogRepository;
    private final JmsTemplate jmsTemplate;

    public CommonCommentService(CommonCommentRepository commentRepository,
                                 AuditLogRepository auditLogRepository,
                                 Optional<JmsTemplate> jmsTemplateOptional) {
        this.commentRepository = commentRepository;
        this.auditLogRepository = auditLogRepository;
        this.jmsTemplate = jmsTemplateOptional.orElse(null);
    }

    @Transactional
    public CommonComment addComment(CommonComment comment) {
        CommonComment saved = commentRepository.save(comment);

        // Send audit message to ActiveMQ JMS Queue
        sendJmsAuditMessage("COMMENT_CREATED", "Comment added for entity " + comment.getEntityId() + " by " + comment.getAuthorRole());

        return saved;
    }

    public List<CommonComment> getCommentsByEntityId(String entityId) {
        return commentRepository.findCommentsByEntityIdNative(entityId);
    }

    public List<CommonComment> getAllComments() {
        return commentRepository.findAll();
    }

    public List<AuditLogMessage> getAllAuditLogs() {
        return auditLogRepository.findAll();
    }

    private void sendJmsAuditMessage(String eventSource, String payload) {
        if (jmsTemplate != null) {
            try {
                jmsTemplate.convertAndSend("ucount.audit.queue", eventSource + "|" + payload);
            } catch (Exception e) {
                // Log and absorb JMS transport errors in local dev execution
            }
        }
    }
}
