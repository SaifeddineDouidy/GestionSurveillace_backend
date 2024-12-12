package com.example.demo.authentication;

import com.example.demo.model.AppUser;
import com.example.demo.repository.AppUserRepository;
import com.example.demo.security.CustomUserDetails;
import com.example.demo.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AppUserRepository appUserRepository;
    private final JwtUtils jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthService(AppUserRepository appUserRepository, JwtUtils jwtUtil, AuthenticationManager authenticationManager) {
        this.appUserRepository = appUserRepository;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    public String authenticate(AuthRequest authenticationRequest) {
        // Authenticate the user
        String email = authenticationRequest.getEmail();
        String password = authenticationRequest.getPassword();

        // Check if user exists
        AppUser appUser = appUserRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("User with email " + email + " not found"));

        // Check if the password matches the storqed one (plain text comparison)
        if (!password.equals(appUser.getPassword())) {
            throw new IllegalStateException("Invalid credentials");
        }

        // Create CustomUserDetails using the appUser entity
        CustomUserDetails customUserDetails = new CustomUserDetails(appUser);

        // Authenticate the user and generate JWT token
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(customUserDetails.getUsername(), authenticationRequest.getPassword())
        );

        // Generate JWT token
        return jwtUtil.generateToken(customUserDetails);
    }
}
