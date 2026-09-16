package com.freddie.ucount.onboarding;

import com.freddie.ucount.onboarding.dto.UserOnboardingRequest;
import com.freddie.ucount.onboarding.entity.Stage1User;
import com.freddie.ucount.onboarding.repository.Stage1UserRepository;
import com.freddie.ucount.onboarding.service.Stage1UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserOnboardingApplicationTests {

    @Autowired
    private Stage1UserService userService;

    @Autowired
    private Stage1UserRepository userRepository;

    @Test
    void testUserRegistration() {
        UserOnboardingRequest req = new UserOnboardingRequest();
        req.setUserId("TST-101");
        req.setOrganizationName("Test Capital Corp");
        req.setTaxId("TX-111");
        req.setWorkEmail("test@test.com");
        req.setContactNumber("555-0100");
        req.setWorkAddress("100 Main St");
        req.setNetworkDomain("test.com");

        Stage1User user = userService.registerUser(req);
        assertNotNull(user.getId());
        assertEquals("PENDING_APPROVAL", user.getApprovalStatus());

        Stage1User fetched = userRepository.findByUserId("TST-101").orElse(null);
        assertNotNull(fetched);
        assertEquals("Test Capital Corp", fetched.getOrganizationName());
    }
}
