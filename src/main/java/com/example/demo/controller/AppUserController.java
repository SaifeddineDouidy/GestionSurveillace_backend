package com.example.demo.controller;

import com.example.demo.authentication.ProfileUpdateRequest;
import com.example.demo.repository.AppUserRepository;
import com.example.demo.security.CustomUserDetails;
import com.example.demo.service.AppUserService;
import com.example.demo.model.AppUser;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.example.demo.utils.ChangePassword;
import java.util.Optional;
import com.example.demo.security.jwt.JwtUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@RestController
@RequestMapping(path = "api/v1/user")
@AllArgsConstructor
@Validated
public class AppUserController {

    private final AppUserService appUserService;
    private final AppUserRepository appUserRepository;

    private final JwtUtils jwtUtil;
    private static final Logger log = LoggerFactory.getLogger(AppUserController.class);
    /**
     * Get the current user's profile information.
     *
     * @return the profile of the currently authenticated user
     */
    @GetMapping("/profile")
    public ResponseEntity<AppUser> getProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Optional<AppUser> appUserOptional = appUserRepository.findByEmail(email);
        return appUserOptional.map(appUser -> {
            AppUser filteredUser = new AppUser();
            filteredUser.setEmail(appUser.getEmail());
            filteredUser.setAppUserRole(appUser.getAppUserRole());
            return ResponseEntity.ok(filteredUser);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }
    /**
     * Update the current user's profile information.
     *
     * @param profileUpdateRequest object containing the updated profile information
     * @return the updated profile
     */
    @PutMapping("/profile")
    public ResponseEntity<Map<String, Object>> updateProfile(@Valid @RequestBody ProfileUpdateRequest profileUpdateRequest) {
        // Log the request payload
        log.info("Received profile update request: {}", profileUpdateRequest);

        // Get the currently authenticated user's email
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        log.info("Authenticated user email: {}", email);

        // Fetch the user by email
        Optional<AppUser> appUserOptional = appUserRepository.findByEmail(email);
        if (appUserOptional.isPresent()) {
            AppUser appUser = appUserOptional.get();
            log.info("User found: {}", appUser);

            // Check if the new email is different from the current one
            if (!appUser.getEmail().equals(profileUpdateRequest.getEmail())) {
                appUser.setEmail(profileUpdateRequest.getEmail());
                log.info("Updated email: {}", profileUpdateRequest.getEmail());
            }

            // Optionally, update the password if provided
            if (profileUpdateRequest.getPassword() != null && !profileUpdateRequest.getPassword().isEmpty()) {
                appUser.setPassword(profileUpdateRequest.getPassword()); // Ideally, hash the password here
                log.info("Updated password for user: {}", appUser.getEmail());
            }

            // Save updated user to the repository
            appUser = appUserRepository.save(appUser);
            log.info("User profile updated successfully!");

            // Create CustomUserDetails from the updated AppUser
            CustomUserDetails customUserDetails = new CustomUserDetails(appUser);

            // Generate a new JWT token with updated information
            String newJwtToken = jwtUtil.generateToken(customUserDetails);

            // Prepare the response data
            Map<String, Object> response = new HashMap<>();
            response.put("token", newJwtToken);
            response.put("user", appUser); // Optionally include updated user details

            // Return the updated user details and the new JWT token in the response body
            return ResponseEntity.ok(response);
        } else {
            log.warn("User not found for email: {}", email);
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/changePassword/{email}")
    public ResponseEntity<String> changePasswordHandler(
            @RequestBody ChangePassword changePassword,
            @PathVariable String email) {

        if (!Objects.equals(changePassword.password(), changePassword.repeatPassword())) {
            return new ResponseEntity<>("Please enter the password again!", HttpStatus.EXPECTATION_FAILED);
        }

        // Save the password in plain text (not recommended for production).
        // Original hashed/encoded version is commented for future use.
        String plainPassword = changePassword.password();
        appUserRepository.updatePassword(email, plainPassword);

        /*
         * Recommended secure approach (commented for reference):
         * String encodedPassword = passwordEncoder.encode(changePassword.password());
         * userRepository.updatePassword(email, encodedPassword);
         */

        return ResponseEntity.ok("Password has been changed");
    }


}
