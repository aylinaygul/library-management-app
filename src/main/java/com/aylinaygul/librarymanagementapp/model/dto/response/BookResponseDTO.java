package com.aylinaygul.librarymanagementapp.model.dto.response;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import com.aylinaygul.librarymanagementapp.model.entity.BorrowRecord;

public record BookResponseDTO(
        UUID id,
        String title,
        String author,
        String genre,
        String isbn,
        LocalDate publicationDate,
        Boolean available,
        List<BorrowRecord> records) {
}
