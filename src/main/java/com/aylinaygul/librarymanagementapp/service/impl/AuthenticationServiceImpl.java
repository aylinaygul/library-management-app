package com.aylinaygul.librarymanagementapp.service.impl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.aylinaygul.librarymanagementapp.model.entity.User;
import com.aylinaygul.librarymanagementapp.model.dto.request.AuthenticationRequest;
import com.aylinaygul.librarymanagementapp.model.dto.request.RegisterRequest;
import com.aylinaygul.librarymanagementapp.model.dto.response.AuthenticationResponse;
import com.aylinaygul.librarymanagementapp.repository.UserRepository;
import com.aylinaygul.librarymanagementapp.security.JwtUtil;
import com.aylinaygul.librarymanagementapp.service.AuthenticationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder().name(request.getName()).email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword())).role(request.getRole())
                .build();

        userRepository.save(user);
        var jwtToken = jwtUtil.generateToken(user);

        return AuthenticationResponse.builder().accessToken(jwtToken).build();
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        var user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        var jwtToken = jwtUtil.generateToken((UserDetails) user);

        return AuthenticationResponse.builder().accessToken(jwtToken).build();
    }
}
