package com.aylinaygul.librarymanagementapp.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.aylinaygul.librarymanagementapp.model.dto.request.AuthenticationRequest;
import com.aylinaygul.librarymanagementapp.model.dto.request.RegisterRequest;
import com.aylinaygul.librarymanagementapp.model.dto.response.AuthenticationResponse;
import com.aylinaygul.librarymanagementapp.model.entity.Role;
import com.aylinaygul.librarymanagementapp.service.AuthenticationService;

@ExtendWith(MockitoExtension.class)
public class AuthenticationControllerTest {

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private AuthenticationController authenticationController;

    @Test
    void shouldLoginUser() {
        AuthenticationRequest authenticationRequest =
                new AuthenticationRequest("testUser", "password123");
        String expectedToken = "jwt-token-here";
        AuthenticationResponse authenticationResponse = new AuthenticationResponse(expectedToken);

        when(authenticationService.authenticate(authenticationRequest))
                .thenReturn(authenticationResponse);

        ResponseEntity<AuthenticationResponse> response =
                authenticationController.login(authenticationRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedToken, response.getBody().getAccessToken());
    }

    @Test
    void shouldRegisterUser() {
        RegisterRequest registerRequest =
                new RegisterRequest("testUser", "password123", "test@example.com", Role.PATRON);
        String successMessage = "User registered successfully";
        AuthenticationResponse authenticationResponse = new AuthenticationResponse(successMessage);

        when(authenticationService.register(registerRequest)).thenReturn(authenticationResponse);

        ResponseEntity<AuthenticationResponse> response =
                authenticationController.register(registerRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(successMessage, response.getBody().getAccessToken());
    }

}
