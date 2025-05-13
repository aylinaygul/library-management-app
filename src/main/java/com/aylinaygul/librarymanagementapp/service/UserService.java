package com.aylinaygul.librarymanagementapp.service;

import com.aylinaygul.librarymanagementapp.model.dto.request.UserUpdateRequest;
import com.aylinaygul.librarymanagementapp.model.dto.response.UserResponse;

import java.util.List;
import java.util.UUID;

public interface UserService {

    List<UserResponse> getAllUsers();

    UserResponse getUserById(UUID userId);

    UserResponse updateUser(UUID userId, UserUpdateRequest updateRequest);

    void deleteUser(UUID userId);
}
