package com.aylinaygul.librarymanagementapp.model.dto.response;

import com.aylinaygul.librarymanagementapp.model.entity.Role;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class UserResponse {
    private UUID id;
    private String name;
    private String email;
    private Role role;
}
