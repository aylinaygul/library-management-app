package com.aylinaygul.librarymanagementapp.model.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateRequest {

    @Size(max = 100, message = "Name must be at most 100 characters")
    private String name;

    @Email(message = "Email should be valid")
    private String email;

}
