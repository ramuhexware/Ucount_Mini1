package com.freddie.ucount.onboarding.dto;

import java.util.List;

public class UserApprovalResponse {
    private String userId;
    private String approvalStatus;
    private String stage2ProfileId;
    private List<String> grantedAccessRights;
    private String message;

    public UserApprovalResponse() {}

    public UserApprovalResponse(String userId, String approvalStatus, String stage2ProfileId, List<String> grantedAccessRights, String message) {
        this.userId = userId;
        this.approvalStatus = approvalStatus;
        this.stage2ProfileId = stage2ProfileId;
        this.grantedAccessRights = grantedAccessRights;
        this.message = message;
    }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getApprovalStatus() { return approvalStatus; }
    public void setApprovalStatus(String approvalStatus) { this.approvalStatus = approvalStatus; }

    public String getStage2ProfileId() { return stage2ProfileId; }
    public void setStage2ProfileId(String stage2ProfileId) { this.stage2ProfileId = stage2ProfileId; }

    public List<String> getGrantedAccessRights() { return grantedAccessRights; }
    public void setGrantedAccessRights(List<String> grantedAccessRights) { this.grantedAccessRights = grantedAccessRights; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
