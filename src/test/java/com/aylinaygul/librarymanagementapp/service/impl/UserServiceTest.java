package com.aylinaygul.librarymanagementapp.service.impl;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.aylinaygul.librarymanagementapp.model.dto.request.UserUpdateRequest;
import com.aylinaygul.librarymanagementapp.model.dto.response.UserResponse;
import com.aylinaygul.librarymanagementapp.model.entity.User;
import com.aylinaygul.librarymanagementapp.model.mapper.UserResponseMapper;
import com.aylinaygul.librarymanagementapp.model.mapper.UserUpdateRequestMapper;
import com.aylinaygul.librarymanagementapp.repository.UserRepository;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserResponseMapper userResponseMapper;

    @Mock
    private UserUpdateRequestMapper userUpdateRequestMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private UUID userId;
    private UserUpdateRequest userUpdateRequest;
    private UserResponse userResponse;

    @BeforeEach
    public void setUp() {
        userId = UUID.randomUUID();
        user = new User(userId, "John Doe", "john.doe@example.com", "password123", null, null);
        userUpdateRequest = new UserUpdateRequest("John Updated", "john.updated@example.com");
        userResponse = new UserResponse(userId, "John Updated", "john.updated@example.com", null);
    }

    @Test
    public void testGetAllUsers() {
        List<User> users = List.of(user);
        List<UserResponse> userResponses = List.of(userResponse);

        when(userRepository.findAll()).thenReturn(users);
        when(userResponseMapper.toDTOList(users)).thenReturn(userResponses);

        List<UserResponse> result = userService.getAllUsers();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(userResponse, result.get(0));
        verify(userRepository, times(1)).findAll();
        verify(userResponseMapper, times(1)).toDTOList(users);
    }

    @Test
    public void testGetUserById() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userResponseMapper.toDTO(user)).thenReturn(userResponse);

        UserResponse result = userService.getUserById(userId);

        assertNotNull(result);
        assertEquals(userResponse, result);
        verify(userRepository, times(1)).findById(userId);
        verify(userResponseMapper, times(1)).toDTO(user);
    }

    @Test
    public void testGetUserById_UserNotFound() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> userService.getUserById(userId));

        assertEquals("User not found with ID: " + userId, exception.getMessage());
        verify(userRepository, times(1)).findById(userId);
    }


    @Test
    public void testUpdateUser() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        doNothing().when(userUpdateRequestMapper).updateUserFromRequest(userUpdateRequest, user);
        when(userRepository.save(user)).thenReturn(user);
        when(userResponseMapper.toDTO(user)).thenReturn(userResponse);

        UserResponse result = userService.updateUser(userId, userUpdateRequest);

        assertNotNull(result);
        assertEquals(userResponse, result);
        verify(userRepository, times(1)).save(user);
        verify(userResponseMapper, times(1)).toDTO(user);
    }

    @Test
    public void testUpdateUser_UserNotFound() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userService.updateUser(userId, userUpdateRequest));
        assertEquals("User not found with ID: " + userId, exception.getMessage());
    }

    @Test
    public void testDeleteUser() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        userService.deleteUser(userId);

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).delete(user);
    }

    @Test
    public void testDeleteUser_UserNotFound() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> userService.deleteUser(userId));
        assertEquals("User not found with ID: " + userId, exception.getMessage());
    }
}
