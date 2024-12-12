package com.example.demo.controller;

import com.example.demo.repository.AppUserRepository;
import com.example.demo.service.AppUserService;
import com.example.demo.model.AppUser;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(path = "api/v1/user")
@AllArgsConstructor
@Validated
public class AppUserController {

    private final AppUserService appUserService;
    private final AppUserRepository appUserRepository;

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


}
