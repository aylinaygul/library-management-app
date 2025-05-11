package com.aylinaygul.librarymanagementapp.model.dto.request;

import java.util.UUID;

import lombok.Data;

@Data
public class BorrowRequest {
    private UUID bookId;
}
