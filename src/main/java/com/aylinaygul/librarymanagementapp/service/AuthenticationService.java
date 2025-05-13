package com.aylinaygul.librarymanagementapp.service;

import com.aylinaygul.librarymanagementapp.model.dto.request.AuthenticationRequest;
import com.aylinaygul.librarymanagementapp.model.dto.request.RegisterRequest;
import com.aylinaygul.librarymanagementapp.model.dto.response.AuthenticationResponse;

public interface AuthenticationService {
        AuthenticationResponse register(RegisterRequest request);

        AuthenticationResponse authenticate(AuthenticationRequest request);
}
