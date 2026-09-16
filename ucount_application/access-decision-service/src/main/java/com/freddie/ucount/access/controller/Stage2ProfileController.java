package com.freddie.ucount.access.controller;

import com.freddie.ucount.access.batch.ControlMAcrBatchJob;
import com.freddie.ucount.access.entity.ArchivedTransaction;
import com.freddie.ucount.access.entity.Stage2Profile;
import com.freddie.ucount.access.entity.UserAccessRight;
import com.freddie.ucount.access.rules.AccessRulesEngine;
import com.freddie.ucount.access.service.Stage2ProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/access")
@CrossOrigin(origins = "*")
public class Stage2ProfileController {

    private final Stage2ProfileService profileService;
    private final AccessRulesEngine rulesEngine;

    public Stage2ProfileController(Stage2ProfileService profileService, AccessRulesEngine rulesEngine) {
        this.profileService = profileService;
        this.rulesEngine = rulesEngine;
    }

    @PostMapping("/profiles")
    public ResponseEntity<Map<String, Object>> createProfile(@RequestBody Map<String, Object> payload) {
        Map<String, Object> result = profileService.createStage2ProfileAndGrantAccess(payload);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/rules/evaluate")
    public ResponseEntity<List<AccessRulesEngine.RuleEntitlement>> evaluateRules(@RequestParam("userType") String userType) {
        return ResponseEntity.ok(rulesEngine.evaluateDefaultEntitlements(userType));
    }

    @GetMapping("/profiles")
    public ResponseEntity<List<Stage2Profile>> getAllProfiles() {
        return ResponseEntity.ok(profileService.getAllProfiles());
    }

    @GetMapping("/profiles/users/{userId}")
    public ResponseEntity<Stage2Profile> getProfileByUserId(@PathVariable("userId") String userId) {
        return profileService.getProfileByUserId(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/rights/users/{userId}")
    public ResponseEntity<List<UserAccessRight>> getRightsByUserId(@PathVariable("userId") String userId) {
        return ResponseEntity.ok(profileService.getAccessRightsByUserId(userId));
    }

    @PostMapping("/batch/run-acr")
    public ResponseEntity<ControlMAcrBatchJob.BatchJobResult> triggerAcrBatch() {
        ControlMAcrBatchJob.BatchJobResult result = profileService.triggerAcrBatchJob();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/archived")
    public ResponseEntity<List<ArchivedTransaction>> getArchivedTransactions() {
        return ResponseEntity.ok(profileService.getArchivedTransactions());
    }
}
