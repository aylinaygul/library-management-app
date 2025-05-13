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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final UserResponseMapper userResponseMapper;
    private final UserUpdateRequestMapper userUpdateRequestMapper;

    @Override
    public List<UserResponse> getAllUsers() {
        logger.info("Fetching all users");
        List<User> users = userRepository.findAll();
        logger.debug("Total users found: {}", users.size());
        return userResponseMapper.toDTOList(users);
    }

    @Override
    public UserResponse getUserById(UUID userId) {
        logger.info("Fetching user by ID: {}", userId);
        User user = userRepository.findById(userId).orElseThrow(() -> {
            logger.warn("User not found with ID: {}", userId);
            return new IllegalArgumentException("User not found with ID: " + userId);
        });
        return userResponseMapper.toDTO(user);
    }

    @Override
    public UserResponse updateUser(UUID userId, UserUpdateRequest updateRequest) {
        logger.info("Updating user with ID: {}", userId);
        User user = userRepository.findById(userId).orElseThrow(() -> {
            logger.warn("User not found for update with ID: {}", userId);
            return new IllegalArgumentException("User not found with ID: " + userId);
        });

        userUpdateRequestMapper.updateUserFromRequest(updateRequest, user);
        User updatedUser = userRepository.save(user);
        logger.info("User updated successfully with ID: {}", userId);
        return userResponseMapper.toDTO(updatedUser);
    }

    @Override
    public void deleteUser(UUID userId) {
        logger.info("Deleting user with ID: {}", userId);
        User user = userRepository.findById(userId).orElseThrow(() -> {
            logger.warn("User not found for deletion with ID: {}", userId);
            return new IllegalArgumentException("User not found with ID: " + userId);
        });
        userRepository.delete(user);
        logger.info("User deleted successfully with ID: {}", userId);
    }
}
