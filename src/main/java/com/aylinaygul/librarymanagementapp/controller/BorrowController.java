package com.aylinaygul.librarymanagementapp.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aylinaygul.librarymanagementapp.service.BorrowService;
import com.aylinaygul.librarymanagementapp.model.dto.response.BorrowResponse;
import com.aylinaygul.librarymanagementapp.model.entity.User;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/borrow")
public class BorrowController {
    private final BorrowService borrowService;

    private UUID getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = (User) userDetails;
        return user.getId();
    }

    @PostMapping("/{bookId}")
    @PreAuthorize("hasRole('PATRON')")
    public ResponseEntity<String> borrowBook(@PathVariable UUID bookId) {
        UUID userId = getAuthenticatedUserId();
        borrowService.borrowBook(userId, bookId);
        return ResponseEntity.ok("Book borrowed successfully.");
    }

    @PostMapping("/return/{bookId}")
    @PreAuthorize("hasRole('PATRON')")
    public ResponseEntity<String> returnBook(@PathVariable UUID bookId) {
        UUID userId = getAuthenticatedUserId();
        borrowService.returnBook(userId, bookId);
        return ResponseEntity.ok("Book returned successfully.");
    }

    // 🔎 GET /borrow/history - Patron views their own history
    @GetMapping("/history")
    @PreAuthorize("hasRole('PATRON')")
    public ResponseEntity<List<BorrowResponse>> getOwnHistory() {
        UUID userId = getAuthenticatedUserId();
        List<BorrowResponse> history = borrowService.getUserBorrowingHistory(userId);
        return ResponseEntity.ok(history);
    }

    // 🔎 GET /borrow/history/{userId} - Librarian views any user's history
    @GetMapping("/history/{userId}")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<List<BorrowResponse>> getUserHistory(@PathVariable UUID userId) {
        List<BorrowResponse> history = borrowService.getUserBorrowingHistory(userId);
        return ResponseEntity.ok(history);
    }

    // 🕒 GET /borrow/overdue - Librarian views all overdue records
    @GetMapping("/overdue")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<List<BorrowResponse>> getOverdueBooks() {
        List<BorrowResponse> overdue = borrowService.getOverdueBooks();
        return ResponseEntity.ok(overdue);
    }
}
