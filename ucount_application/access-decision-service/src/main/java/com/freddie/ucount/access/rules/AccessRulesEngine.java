package com.freddie.ucount.access.rules;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AccessRulesEngine {

    public List<RuleEntitlement> evaluateDefaultEntitlements(String userType) {
        List<RuleEntitlement> entitlements = new ArrayList<>();

        if (userType == null) {
            userType = "HOUSE_SELLER";
        }

        switch (userType.toUpperCase()) {
            case "HOUSE_SELLER":
                entitlements.add(new RuleEntitlement("LOAN_ORIGINATION_PORTAL", "FULL_ACCESS"));
                entitlements.add(new RuleEntitlement("PROPERTY_APPRAISAL_GATEWAY", "FULL_ACCESS"));
                entitlements.add(new RuleEntitlement("TITLE_INSURANCE_PORTAL", "READ_ONLY"));
                break;

            case "HOUSE_BUYER":
                entitlements.add(new RuleEntitlement("LOAN_APPLICATION_PORTAL", "FULL_ACCESS"));
                entitlements.add(new RuleEntitlement("CREDIT_BUREAU_PORTAL", "READ_ONLY"));
                entitlements.add(new RuleEntitlement("DOCUMENT_VAULT", "FULL_ACCESS"));
                break;

            case "INSURANCE_AGENT":
                entitlements.add(new RuleEntitlement("TITLE_INSURANCE_PORTAL", "FULL_ACCESS"));
                entitlements.add(new RuleEntitlement("ESCROW_MANAGEMENT_PORTAL", "FULL_ACCESS"));
                entitlements.add(new RuleEntitlement("LOAN_ORIGINATION_PORTAL", "RESTRICTED"));
                break;

            case "MORTGAGE_SERVICER":
                entitlements.add(new RuleEntitlement("LOAN_SERVICING_SYSTEM", "FULL_ACCESS"));
                entitlements.add(new RuleEntitlement("SECONDARY_MARKET_ACCESS", "FULL_ACCESS"));
                entitlements.add(new RuleEntitlement("COMPLIANCE_AUDIT_LOGS", "READ_ONLY"));
                break;

            default:
                entitlements.add(new RuleEntitlement("GENERAL_USER_PORTAL", "READ_ONLY"));
                break;
        }

        return entitlements;
    }

    public static class RuleEntitlement {
        private final String serviceName;
        private final String permissionLevel;

        public RuleEntitlement(String serviceName, String permissionLevel) {
            this.serviceName = serviceName;
            this.permissionLevel = permissionLevel;
        }

        public String getServiceName() { return serviceName; }
        public String getPermissionLevel() { return permissionLevel; }
    }
}
