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

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/borrow")
@Tag(name = "Borrow Records", description = "API endpoints for borrow management")
public class BorrowController {
    private final BorrowService borrowService;

    private UUID getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = (User) userDetails;
        return user.getId();
    }

    @PostMapping("/{bookId}")
    @PreAuthorize("hasRole('ROLE_PATRON')")
    @Operation(summary = "Borrow a book",
            description = "Allows an authenticated patron to borrow a book by providing the book ID")
    public ResponseEntity<String> borrowBook(@PathVariable UUID bookId) {
        UUID userId = getAuthenticatedUserId();
        borrowService.borrowBook(userId, bookId);
        return ResponseEntity.ok("Book borrowed successfully.");
    }

    @PostMapping("/return/{bookId}")
    @PreAuthorize("hasRole('ROLE_PATRON')")
    @Operation(summary = "Return a borrowed book",
            description = "Allows an authenticated patron to return a previously borrowed book")
    public ResponseEntity<String> returnBook(@PathVariable UUID bookId) {
        UUID userId = getAuthenticatedUserId();
        borrowService.returnBook(userId, bookId);
        return ResponseEntity.ok("Book returned successfully.");
    }

    // 🔎 GET /borrow/history - Patron views their own history
    @GetMapping("/history")
    @PreAuthorize("hasRole('ROLE_PATRON')")
    @Operation(summary = "Get authenticated user's borrow history",
            description = "Retrieves the borrow history of the currently authenticated patron")
    public ResponseEntity<List<BorrowResponse>> getOwnHistory() {
        UUID userId = getAuthenticatedUserId();
        List<BorrowResponse> history = borrowService.getUserBorrowingHistory(userId);
        return ResponseEntity.ok(history);
    }

    // 🔎 GET /borrow/history/{userId} - Librarian views any user's history
    @GetMapping("/history/{userId}")
    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    @Operation(summary = "Get a specific user's borrow history",
            description = "Allows a librarian to retrieve the borrow history of a specific user by user ID")
    public ResponseEntity<List<BorrowResponse>> getUserHistory(@PathVariable UUID userId) {
        List<BorrowResponse> history = borrowService.getUserBorrowingHistory(userId);
        return ResponseEntity.ok(history);
    }

    // 🕒 GET /borrow/overdue - Librarian views all overdue records
    @GetMapping("/overdue")
    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    @Operation(summary = "Get all overdue borrowed books",
            description = "Allows a librarian to retrieve a list of all overdue borrowed books")
    public ResponseEntity<List<BorrowResponse>> getOverdueBooks() {
        List<BorrowResponse> overdue = borrowService.getOverdueBooks();
        return ResponseEntity.ok(overdue);
    }
}
