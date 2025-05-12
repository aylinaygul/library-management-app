package com.aylinaygul.librarymanagementapp.model.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class BorrowRequest {

    @NotNull(message = "Book ID is required.")
    private UUID bookId;
}
