package com.aylinaygul.librarymanagementapp.service;

import com.aylinaygul.librarymanagementapp.model.dto.request.UserUpdateRequest;
import com.aylinaygul.librarymanagementapp.model.dto.response.UserResponse;
import com.aylinaygul.librarymanagementapp.model.entity.User;
import com.aylinaygul.librarymanagementapp.model.mapper.UserResponseMapper;
import com.aylinaygul.librarymanagementapp.model.mapper.UserUpdateRequestMapper;
import com.aylinaygul.librarymanagementapp.repository.UserRepository;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserResponseMapper userResponseMapper;
    private final UserUpdateRequestMapper userUpdateRequestMapper;

    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        return userResponseMapper.toDTOList(users);
    }

    public UserResponse getUserById(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
        return userResponseMapper.toDTO(user);
    }

    public UserResponse updateUser(UUID userId, UserUpdateRequest updateRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
        userUpdateRequestMapper.updateUserFromRequest(updateRequest, user);
        User updatedUser = userRepository.save(user);
        return userResponseMapper.toDTO(updatedUser);
    }

    public void deleteUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
        userRepository.delete(user);
    }
}
