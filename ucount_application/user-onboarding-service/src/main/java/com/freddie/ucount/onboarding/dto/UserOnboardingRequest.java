package com.freddie.ucount.onboarding.dto;

public class UserOnboardingRequest {
    private String userId;
    private String organizationName;
    private String taxId;
    private String workEmail;
    private String contactNumber;
    private String workAddress;
    private String networkDomain;
    private String userType; // HOUSE_SELLER, HOUSE_BUYER, INSURANCE_AGENT, MORTGAGE_SERVICER

    public UserOnboardingRequest() {}

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getOrganizationName() { return organizationName; }
    public void setOrganizationName(String organizationName) { this.organizationName = organizationName; }

    public String getTaxId() { return taxId; }
    public void setTaxId(String taxId) { this.taxId = taxId; }

    public String getWorkEmail() { return workEmail; }
    public void setWorkEmail(String workEmail) { this.workEmail = workEmail; }

    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }

    public String getWorkAddress() { return workAddress; }
    public void setWorkAddress(String workAddress) { this.workAddress = workAddress; }

    public String getNetworkDomain() { return networkDomain; }
    public void setNetworkDomain(String networkDomain) { this.networkDomain = networkDomain; }

    public String getUserType() { return userType; }
    public void setUserType(String userType) { this.userType = userType; }
}
