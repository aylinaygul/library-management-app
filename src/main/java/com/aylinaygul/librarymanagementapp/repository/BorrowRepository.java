package com.aylinaygul.librarymanagementapp.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aylinaygul.librarymanagementapp.model.entity.BorrowRecord;
import com.aylinaygul.librarymanagementapp.model.entity.BorrowStatus;

public interface BorrowRepository extends JpaRepository<BorrowRecord, UUID> {
    List<BorrowRecord> findByUserId(UUID id);

    List<BorrowRecord> findByStatus(BorrowStatus status);

    Optional<BorrowRecord> findByBookIdAndStatus(UUID bookId, BorrowStatus status);

    Optional<BorrowRecord> findByUserIdAndBookIdAndStatus(UUID userId, UUID bookId, BorrowStatus status);

    List<BorrowRecord> findAllByUserIdOrderByBorrowDateDesc(UUID userId);

    List<BorrowRecord> findAllByOrderByBorrowDateDesc();

    List<BorrowRecord> findByStatusAndDueDateBefore(BorrowStatus status, LocalDate date);

}