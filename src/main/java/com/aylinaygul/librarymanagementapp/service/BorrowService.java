package com.aylinaygul.librarymanagementapp.service;

import java.util.List;
import java.util.UUID;

import com.aylinaygul.librarymanagementapp.model.dto.response.BorrowResponse;

public interface BorrowService {

    void borrowBook(UUID userId, UUID bookId);

    void returnBook(UUID userId, UUID bookId);

    List<BorrowResponse> getUserBorrowingHistory(UUID userId);

    List<BorrowResponse> getAllBorrowHistory();

    List<BorrowResponse> getOverdueBooks();
}
