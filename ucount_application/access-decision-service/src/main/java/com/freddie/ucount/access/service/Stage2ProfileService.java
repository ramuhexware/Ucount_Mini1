package com.freddie.ucount.access.service;

import com.freddie.ucount.access.batch.ControlMAcrBatchJob;
import com.freddie.ucount.access.entity.ArchivedTransaction;
import com.freddie.ucount.access.entity.Stage2Profile;
import com.freddie.ucount.access.entity.UserAccessRight;
import com.freddie.ucount.access.repository.ArchivedTransactionRepository;
import com.freddie.ucount.access.repository.Stage2ProfileRepository;
import com.freddie.ucount.access.repository.UserAccessRightRepository;
import com.freddie.ucount.access.rules.AccessRulesEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class Stage2ProfileService {

    private static final Logger log = LoggerFactory.getLogger(Stage2ProfileService.class);

    private final Stage2ProfileRepository profileRepository;
    private final UserAccessRightRepository accessRightRepository;
    private final ArchivedTransactionRepository archivedRepository;
    private final AccessRulesEngine rulesEngine;
    private final ControlMAcrBatchJob acrBatchJob;

    public Stage2ProfileService(Stage2ProfileRepository profileRepository,
                                 UserAccessRightRepository accessRightRepository,
                                 ArchivedTransactionRepository archivedRepository,
                                 AccessRulesEngine rulesEngine,
                                 ControlMAcrBatchJob acrBatchJob) {
        this.profileRepository = profileRepository;
        this.accessRightRepository = accessRightRepository;
        this.archivedRepository = archivedRepository;
        this.rulesEngine = rulesEngine;
        this.acrBatchJob = acrBatchJob;
    }

    @Transactional
    public Map<String, Object> createStage2ProfileAndGrantAccess(Map<String, Object> requestPayload) {
        String userId = (String) requestPayload.get("userId");
        String userType = (String) requestPayload.getOrDefault("userType", "HOUSE_SELLER");
        String profileId = "PRFL-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        log.info("Creating Stage 2 Profile for User ID: {}, Profile ID: {}, Type: {}", userId, profileId, userType);

        Stage2Profile profile = new Stage2Profile();
        profile.setProfileId(profileId);
        profile.setUserId(userId);
        profile.setUserType(userType);
        profile.setFinancialHistoryRating("TIER_1_PRIME");
        profile.setAnnualRevenue(new BigDecimal("5000000.00"));
        profile.setYearsInBusiness(5);
        profile.setCompliancePassed(true);
        profile.setStatus("ACTIVE");

        Stage2Profile savedProfile = profileRepository.save(profile);

        // Evaluate Rules Engine for Default Access Rights
        List<AccessRulesEngine.RuleEntitlement> entitlements = rulesEngine.evaluateDefaultEntitlements(userType);
        List<String> grantedRights = new ArrayList<>();

        for (AccessRulesEngine.RuleEntitlement entitlement : entitlements) {
            UserAccessRight right = new UserAccessRight(profileId, userId, entitlement.getServiceName(), entitlement.getPermissionLevel());
            accessRightRepository.save(right);
            grantedRights.add(entitlement.getServiceName() + " (" + entitlement.getPermissionLevel() + ")");
        }

        Map<String, Object> response = new HashMap<>();
        response.put("profileId", profileId);
        response.put("userId", userId);
        response.put("userType", userType);
        response.put("status", savedProfile.getStatus());
        response.put("grantedRights", grantedRights);

        return response;
    }

    public List<Stage2Profile> getAllProfiles() {
        return profileRepository.findAll();
    }

    public Optional<Stage2Profile> getProfileByUserId(String userId) {
        return profileRepository.findByUserId(userId);
    }

    public List<UserAccessRight> getAccessRightsByUserId(String userId) {
        return accessRightRepository.findByUserId(userId);
    }

    public ControlMAcrBatchJob.BatchJobResult triggerAcrBatchJob() {
        return acrBatchJob.runAcrBatchJob();
    }

    public List<ArchivedTransaction> getArchivedTransactions() {
        return archivedRepository.findAll();
    }
}
