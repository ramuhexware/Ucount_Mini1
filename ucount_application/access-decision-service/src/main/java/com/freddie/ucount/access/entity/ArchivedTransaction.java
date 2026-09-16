package com.freddie.ucount.access.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ucount_archived_transactions")
public class ArchivedTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "original_profile_id", nullable = false)
    private String originalProfileId;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "user_type", nullable = false)
    private String userType;

    @Column(name = "archive_reason", nullable = false)
    private String archiveReason;

    @Column(name = "archived_at")
    private LocalDateTime archivedAt;

    @Column(name = "payload_json", columnDefinition = "TEXT")
    private String payloadJson;

    public ArchivedTransaction() {}

    public ArchivedTransaction(String originalProfileId, String userId, String userType, String archiveReason, String payloadJson) {
        this.originalProfileId = originalProfileId;
        this.userId = userId;
        this.userType = userType;
        this.archiveReason = archiveReason;
        this.payloadJson = payloadJson;
        this.archivedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getOriginalProfileId() { return originalProfileId; }
    public void setOriginalProfileId(String originalProfileId) { this.originalProfileId = originalProfileId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getUserType() { return userType; }
    public void setUserType(String userType) { this.userType = userType; }

    public String getArchiveReason() { return archiveReason; }
    public void setArchiveReason(String archiveReason) { this.archiveReason = archiveReason; }

    public LocalDateTime getArchivedAt() { return archivedAt; }
    public void setArchivedAt(LocalDateTime archivedAt) { this.archivedAt = archivedAt; }

    public String getPayloadJson() { return payloadJson; }
    public void setPayloadJson(String payloadJson) { this.payloadJson = payloadJson; }
}
