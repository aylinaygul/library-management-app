package com.aylinaygul.librarymanagementapp.controller;

import com.aylinaygul.librarymanagementapp.model.dto.request.UserUpdateRequest;
import com.aylinaygul.librarymanagementapp.model.dto.response.UserResponse;
import com.aylinaygul.librarymanagementapp.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

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
        return ResponseEntity.ok(userService.getAllUsers());
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
        return ResponseEntity.ok(userService.getUserById(id));
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
        return ResponseEntity.ok(userService.updateUser(id, request));
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
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
