package com.freddie.ucount.access.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ucount_user_access_rights")
public class UserAccessRight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "profile_id", nullable = false)
    private String profileId;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "service_name", nullable = false)
    private String serviceName;

    @Column(name = "permission_level", nullable = false)
    private String permissionLevel; // READ_ONLY, FULL_ACCESS, RESTRICTED

    @Column(name = "granted_at")
    private LocalDateTime grantedAt;

    public UserAccessRight() {}

    public UserAccessRight(String profileId, String userId, String serviceName, String permissionLevel) {
        this.profileId = profileId;
        this.userId = userId;
        this.serviceName = serviceName;
        this.permissionLevel = permissionLevel;
        this.grantedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getProfileId() { return profileId; }
    public void setProfileId(String profileId) { this.profileId = profileId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getServiceName() { return serviceName; }
    public void setServiceName(String serviceName) { this.serviceName = serviceName; }

    public String getPermissionLevel() { return permissionLevel; }
    public void setPermissionLevel(String permissionLevel) { this.permissionLevel = permissionLevel; }

    public LocalDateTime getGrantedAt() { return grantedAt; }
    public void setGrantedAt(LocalDateTime grantedAt) { this.grantedAt = grantedAt; }
}
