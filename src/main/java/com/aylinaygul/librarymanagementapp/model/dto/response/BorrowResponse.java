package com.aylinaygul.librarymanagementapp.model.dto.response;

import java.time.LocalDate;
import java.util.UUID;

import com.aylinaygul.librarymanagementapp.model.entity.BorrowStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BorrowResponse {
    private UUID id;
    private UUID userId;
    private UUID bookId;
    private String bookTitle;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private BorrowStatus status;
}
