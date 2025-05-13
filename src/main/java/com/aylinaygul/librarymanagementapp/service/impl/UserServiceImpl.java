package com.aylinaygul.librarymanagementapp.service.impl;

import com.aylinaygul.librarymanagementapp.model.dto.request.UserUpdateRequest;
import com.aylinaygul.librarymanagementapp.model.dto.response.UserResponse;
import com.aylinaygul.librarymanagementapp.model.entity.User;
import com.aylinaygul.librarymanagementapp.model.mapper.UserResponseMapper;
import com.aylinaygul.librarymanagementapp.model.mapper.UserUpdateRequestMapper;
import com.aylinaygul.librarymanagementapp.repository.UserRepository;
import com.aylinaygul.librarymanagementapp.service.UserService;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserResponseMapper userResponseMapper;
    private final UserUpdateRequestMapper userUpdateRequestMapper;

    @Override
    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        return userResponseMapper.toDTOList(users);
    }

    @Override
    public UserResponse getUserById(UUID userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("User not found with ID: " + userId));
        return userResponseMapper.toDTO(user);
    }

    @Override
    public UserResponse updateUser(UUID userId, UserUpdateRequest updateRequest) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("User not found with ID: " + userId));
        userUpdateRequestMapper.updateUserFromRequest(updateRequest, user);
        User updatedUser = userRepository.save(user);
        return userResponseMapper.toDTO(updatedUser);
    }

    @Override
    public void deleteUser(UUID userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("User not found with ID: " + userId));
        userRepository.delete(user);
    }
}
