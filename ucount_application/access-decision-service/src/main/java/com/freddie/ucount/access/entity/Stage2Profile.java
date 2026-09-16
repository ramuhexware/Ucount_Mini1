package com.freddie.ucount.access.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "ucount_stage2_profiles")
public class Stage2Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "profile_id", unique = true, nullable = false)
    private String profileId;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "user_type", nullable = false)
    private String userType; // HOUSE_SELLER, HOUSE_BUYER, INSURANCE_AGENT, MORTGAGE_SERVICER

    @Column(name = "financial_history_rating", nullable = false)
    private String financialHistoryRating;

    @Column(name = "annual_revenue")
    private BigDecimal annualRevenue;

    @Column(name = "years_in_business")
    private Integer yearsInBusiness;

    @Column(name = "compliance_passed")
    private Boolean compliancePassed;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Stage2Profile() {}

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.status == null) {
            this.status = "ACTIVE";
        }
        if (this.compliancePassed == null) {
            this.compliancePassed = true;
        }
        if (this.financialHistoryRating == null) {
            this.financialHistoryRating = "TIER_1_PRIME";
        }
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getProfileId() { return profileId; }
    public void setProfileId(String profileId) { this.profileId = profileId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getUserType() { return userType; }
    public void setUserType(String userType) { this.userType = userType; }

    public String getFinancialHistoryRating() { return financialHistoryRating; }
    public void setFinancialHistoryRating(String financialHistoryRating) { this.financialHistoryRating = financialHistoryRating; }

    public BigDecimal getAnnualRevenue() { return annualRevenue; }
    public void setAnnualRevenue(BigDecimal annualRevenue) { this.annualRevenue = annualRevenue; }

    public Integer getYearsInBusiness() { return yearsInBusiness; }
    public void setYearsInBusiness(Integer yearsInBusiness) { this.yearsInBusiness = yearsInBusiness; }

    public Boolean getCompliancePassed() { return compliancePassed; }
    public void setCompliancePassed(Boolean compliancePassed) { this.compliancePassed = compliancePassed; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
