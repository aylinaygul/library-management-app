package com.aylinaygul.librarymanagementapp.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aylinaygul.librarymanagementapp.model.dto.request.RegisterRequest;
import com.aylinaygul.librarymanagementapp.model.dto.request.AuthenticationRequest;
import com.aylinaygul.librarymanagementapp.model.dto.response.AuthenticationResponse;
import com.aylinaygul.librarymanagementapp.service.AuthenticationService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication", description = "API endpoints for authentication")
public class AuthenticationController {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    @Operation(summary = "Register a new user",
            description = "Registers a new user to the system. The user details must be provided in the request body.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully registered the user"),
            @ApiResponse(responseCode = "400", description = "Invalid registration data"),
            @ApiResponse(responseCode = "409", description = "User already exists")})
    public ResponseEntity<AuthenticationResponse> register(
            @Valid @RequestBody RegisterRequest request) {

        logger.info("Registering new user with email: {}", request.getEmail());
        AuthenticationResponse response = authenticationService.register(request);
        logger.info("User registered successfully: {}", request.getEmail());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticate a user",
            description = "Authenticates a user and returns a JWT token for future requests.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully authenticated the user"),
            @ApiResponse(responseCode = "400", description = "Invalid login credentials"),
            @ApiResponse(responseCode = "404", description = "User not found")})
    public ResponseEntity<AuthenticationResponse> login(
            @Valid @RequestBody AuthenticationRequest request) {

        logger.info("User attempting login with email: {}", request.getEmail());
        AuthenticationResponse response = authenticationService.authenticate(request);
        logger.info("User authenticated successfully: {}", request.getEmail());

        return ResponseEntity.ok(response);
    }
}
