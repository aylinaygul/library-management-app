package com.aylinaygul.librarymanagementapp.controller;

import com.aylinaygul.librarymanagementapp.model.dto.request.UserUpdateRequest;
import com.aylinaygul.librarymanagementapp.model.dto.response.UserResponse;
import com.aylinaygul.librarymanagementapp.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@PreAuthorize("hasRole('ROLE_LIBRARIAN')")
@Tag(name = "Users", description = "API endpoints for user CRUD operations")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    @GetMapping
    @Operation(summary = "Get all users",
            description = "Retrieves a list of all registered users. Only accessible by librarians.")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Successfully retrieved the list of users"),
            @ApiResponse(responseCode = "403",
                    description = "Forbidden - Only librarians can access this endpoint")})
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        logger.info("Librarian requested list of all users");
        List<UserResponse> users = userService.getAllUsers();
        logger.debug("Total users found: {}", users.size());
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID",
            description = "Retrieves the details of a specific user by their ID. Only accessible by librarians.")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Successfully retrieved the user details"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "403",
                    description = "Forbidden - Only librarians can access this endpoint")})
    public ResponseEntity<UserResponse> getUserById(@PathVariable UUID id) {
        logger.info("Librarian requested details for user with ID {}", id);
        UserResponse user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update user details",
            description = "Updates the information of a specific user by their ID. Only accessible by librarians.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully updated the user"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "403",
                    description = "Forbidden - Only librarians can access this endpoint")})
    public ResponseEntity<UserResponse> updateUser(@PathVariable UUID id,
            @Valid @RequestBody UserUpdateRequest request) {
        logger.info("Librarian is updating user with ID {}", id);
        logger.debug("Update request: {}", request);
        UserResponse updatedUser = userService.updateUser(id, request);
        logger.info("Successfully updated user with ID {}", id);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a user",
            description = "Deletes a specific user by their ID from the system. Only accessible by librarians.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Successfully deleted the user"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "403",
                    description = "Forbidden - Only librarians can access this endpoint")})
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        logger.warn("Librarian is attempting to delete user with ID {}", id);
        userService.deleteUser(id);
        logger.info("Successfully deleted user with ID {}", id);
        return ResponseEntity.noContent().build();
    }
}
