package com.freddie.ucount.notification.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ucount_common_comments")
public class CommonComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "entity_id", nullable = false)
    private String entityId; // User ID or Profile ID

    @Column(name = "service_origin", nullable = false)
    private String serviceOrigin; // ONBOARDING_SERVICE, ACCESS_DECISION_SERVICE, COMPLIANCE

    @Column(name = "author_role", nullable = false)
    private String authorRole; // PA_PO, LEAD_DEV, AUDITOR

    @Column(name = "comment_text", columnDefinition = "TEXT", nullable = false)
    private String commentText;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public CommonComment() {}

    public CommonComment(String entityId, String serviceOrigin, String authorRole, String commentText) {
        this.entityId = entityId;
        this.serviceOrigin = serviceOrigin;
        this.authorRole = authorRole;
        this.commentText = commentText;
        this.createdAt = LocalDateTime.now();
    }

    @PrePersist
    public void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEntityId() { return entityId; }
    public void setEntityId(String entityId) { this.entityId = entityId; }

    public String getServiceOrigin() { return serviceOrigin; }
    public void setServiceOrigin(String serviceOrigin) { this.serviceOrigin = serviceOrigin; }

    public String getAuthorRole() { return authorRole; }
    public void setAuthorRole(String authorRole) { this.authorRole = authorRole; }

    public String getCommentText() { return commentText; }
    public void setCommentText(String commentText) { this.commentText = commentText; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
