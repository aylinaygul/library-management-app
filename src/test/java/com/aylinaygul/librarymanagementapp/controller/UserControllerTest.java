package com.aylinaygul.librarymanagementapp.controller;

import com.aylinaygul.librarymanagementapp.model.dto.request.UserUpdateRequest;
import com.aylinaygul.librarymanagementapp.model.dto.response.UserResponse;
import com.aylinaygul.librarymanagementapp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    private UUID userId;
    private UserResponse userResponse;
    private UserUpdateRequest userUpdateRequest;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();

        userResponse = UserResponse.builder().id(userId).name("John Doe")
                .email("john.doe@example.com").build();

        userUpdateRequest = new UserUpdateRequest();
        userUpdateRequest.setName("John Doe");
        userUpdateRequest.setEmail("john.doe@example.com");
    }

    @Test
    void testGetAllUsers() {
        List<UserResponse> userList = List.of(userResponse);
        when(userService.getAllUsers()).thenReturn(userList);

        ResponseEntity<List<UserResponse>> response = userController.getAllUsers();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(userList, response.getBody());
    }

    @Test
    void testGetUserById() {
        when(userService.getUserById(userId)).thenReturn(userResponse);

        ResponseEntity<UserResponse> response = userController.getUserById(userId);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(userResponse, response.getBody());
    }

    @Test
    void testUpdateUser() {
        when(userService.updateUser(userId, userUpdateRequest)).thenReturn(userResponse);

        ResponseEntity<UserResponse> response =
                userController.updateUser(userId, userUpdateRequest);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(userResponse, response.getBody());
    }

    @Test
    void testDeleteUser() {
        doNothing().when(userService).deleteUser(userId);

        ResponseEntity<Void> response = userController.deleteUser(userId);

        assertNotNull(response);
        assertEquals(204, response.getStatusCodeValue());
        verify(userService, times(1)).deleteUser(userId);
    }
}
