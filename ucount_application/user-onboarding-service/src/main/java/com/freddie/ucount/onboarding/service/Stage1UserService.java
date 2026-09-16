package com.freddie.ucount.onboarding.service;

import com.freddie.ucount.onboarding.dto.UserApprovalResponse;
import com.freddie.ucount.onboarding.dto.UserOnboardingRequest;
import com.freddie.ucount.onboarding.entity.Stage1User;
import com.freddie.ucount.onboarding.repository.Stage1UserRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class Stage1UserService {

    private static final Logger log = LoggerFactory.getLogger(Stage1UserService.class);

    private final Stage1UserRepository userRepository;
    private final WebClient.Builder webClientBuilder;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${services.access-decision-service.url:http://localhost:8082}")
    private String accessDecisionServiceUrl;

    public Stage1UserService(Stage1UserRepository userRepository,
                              WebClient.Builder webClientBuilder,
                              Optional<KafkaTemplate<String, String>> kafkaTemplateOptional) {
        this.userRepository = userRepository;
        this.webClientBuilder = webClientBuilder;
        this.kafkaTemplate = kafkaTemplateOptional.orElse(null);
    }

    @Transactional
    public Stage1User registerUser(UserOnboardingRequest request) {
        log.info("Registering Stage 1 User: {}", request.getUserId());

        Stage1User user = new Stage1User();
        user.setUserId(request.getUserId());
        user.setOrganizationName(request.getOrganizationName());
        user.setTaxId(request.getTaxId());
        user.setWorkEmail(request.getWorkEmail());
        user.setContactNumber(request.getContactNumber());
        user.setWorkAddress(request.getWorkAddress());
        user.setNetworkDomain(request.getNetworkDomain());
        user.setApprovalStatus("PENDING_APPROVAL");
        user.setInvestigationNotes("Stage 1 details captured. Pending compliance investigation.");

        Stage1User savedUser = userRepository.save(user);

        // Publish event to Kafka
        publishKafkaEvent("USER_REGISTERED", savedUser.getUserId());

        return savedUser;
    }

    @Transactional
    @CircuitBreaker(name = "accessDecisionService", fallbackMethod = "approveUserFallback")
    public UserApprovalResponse approveUserAndPromoteToStage2(String userId, String userType) {
        log.info("Approving Stage 1 User {} and invoking Stage 2 Access Decision Service", userId);

        Stage1User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));

        user.setApprovalStatus("APPROVED");
        user.setInvestigationNotes("Approved by Freddie Mac Security. Promoting to Stage 2 Counterparty Profile.");
        userRepository.save(user);

        // WebClient Inter-service call to access-decision-service
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("userId", userId);
        requestBody.put("userType", userType != null ? userType : "HOUSE_SELLER");
        requestBody.put("organizationName", user.getOrganizationName());

        Map responseMap = webClientBuilder.build()
                .post()
                .uri(accessDecisionServiceUrl + "/api/v1/access/profiles")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        String profileId = responseMap != null && responseMap.containsKey("profileId") ? 
                (String) responseMap.get("profileId") : "PRFL-" + System.currentTimeMillis();
        List<String> rights = responseMap != null && responseMap.containsKey("grantedRights") ?
                (List<String>) responseMap.get("grantedRights") : List.of("LOAN_ORIGINATION_PORTAL", "SECONDARY_MARKET_ACCESS");

        // Publish event to Kafka
        publishKafkaEvent("USER_APPROVED", userId);

        return new UserApprovalResponse(userId, "APPROVED", profileId, rights, "User successfully approved and Stage 2 access rights provisioned.");
    }

    public UserApprovalResponse approveUserFallback(String userId, String userType, Throwable throwable) {
        log.warn("Circuit Breaker activated for Stage 2 call for user {}. Reason: {}", userId, throwable.getMessage());
        return new UserApprovalResponse(userId, "APPROVED_PENDING_STAGE2", "FALLBACK-TEMP",
                List.of("READ_ONLY_PORTAL"), "User approved in Stage 1, but Stage 2 provisioning is currently queued (Fallback Mode).");
    }

    public Optional<Stage1User> getUserByUserId(String userId) {
        return userRepository.findByUserId(userId);
    }

    public List<Stage1User> getAllUsers() {
        return userRepository.findAll();
    }

    private void publishKafkaEvent(String eventType, String userId) {
        if (kafkaTemplate != null) {
            try {
                kafkaTemplate.send("ucount-user-events", userId, eventType + ":" + userId);
                log.info("Published Kafka event {} for user {}", eventType, userId);
            } catch (Exception e) {
                log.warn("Could not publish event to Kafka broker: {}", e.getMessage());
            }
        }
    }
}
