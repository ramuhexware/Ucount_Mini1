package com.freddie.ucount.access;

import com.freddie.ucount.access.rules.AccessRulesEngine;
import com.freddie.ucount.access.service.Stage2ProfileService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AccessDecisionApplicationTests {

    @Autowired
    private Stage2ProfileService profileService;

    @Autowired
    private AccessRulesEngine rulesEngine;

    @Test
    void testRulesEngineEvaluation() {
        List<AccessRulesEngine.RuleEntitlement> entitlements = rulesEngine.evaluateDefaultEntitlements("HOUSE_SELLER");
        assertFalse(entitlements.isEmpty());
        assertTrue(entitlements.stream().anyMatch(e -> e.getServiceName().equals("LOAN_ORIGINATION_PORTAL")));
    }

    @Test
    void testStage2ProfileCreation() {
        Map<String, Object> req = new HashMap<>();
        req.put("userId", "TST-202");
        req.put("userType", "MORTGAGE_SERVICER");

        Map<String, Object> res = profileService.createStage2ProfileAndGrantAccess(req);
        assertNotNull(res.get("profileId"));
        assertEquals("MORTGAGE_SERVICER", res.get("userType"));
    }
}
