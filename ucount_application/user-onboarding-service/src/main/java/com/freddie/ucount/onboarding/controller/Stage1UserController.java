package com.freddie.ucount.onboarding.controller;

import com.freddie.ucount.onboarding.dto.UserApprovalResponse;
import com.freddie.ucount.onboarding.dto.UserOnboardingRequest;
import com.freddie.ucount.onboarding.entity.Stage1User;
import com.freddie.ucount.onboarding.service.Stage1UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/onboarding")
@CrossOrigin(origins = "*")
public class Stage1UserController {

    private final Stage1UserService userService;

    public Stage1UserController(Stage1UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<Stage1User> registerUser(@RequestBody UserOnboardingRequest request) {
        Stage1User user = userService.registerUser(request);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/users/{userId}/approve")
    public ResponseEntity<UserApprovalResponse> approveUser(
            @PathVariable("userId") String userId,
            @RequestParam(value = "userType", required = false, defaultValue = "HOUSE_SELLER") String userType) {
        UserApprovalResponse response = userService.approveUserAndPromoteToStage2(userId, userType);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<Stage1User> getUserByUserId(@PathVariable("userId") String userId) {
        return userService.getUserByUserId(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/users")
    public ResponseEntity<List<Stage1User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
}
