package com.aylinaygul.librarymanagementapp.controller;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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

    private static final Logger logger = LoggerFactory.getLogger(BorrowController.class);

    private final BorrowService borrowService;

    private UUID getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            if (userDetails instanceof User) {
                User user = (User) userDetails;
                logger.debug("Authenticated user ID retrieved: {}", user.getId());
                return user.getId();
            }
        }
        logger.error("Failed to retrieve authenticated user.");
        throw new RuntimeException("Authenticated user not found.");
    }

    @PostMapping("/{bookId}")
    @PreAuthorize("hasRole('ROLE_PATRON')")
    @Operation(summary = "Borrow a book",
            description = "Allows an authenticated patron to borrow a book by providing the book ID")
    public ResponseEntity<String> borrowBook(@PathVariable UUID bookId) {
        UUID userId = getAuthenticatedUserId();
        logger.info("User {} is attempting to borrow book {}", userId, bookId);
        borrowService.borrowBook(userId, bookId);
        logger.info("User {} successfully borrowed book {}", userId, bookId);
        return ResponseEntity.ok("Book borrowed successfully.");
    }

    @PostMapping("/return/{bookId}")
    @PreAuthorize("hasRole('ROLE_PATRON')")
    @Operation(summary = "Return a borrowed book",
            description = "Allows an authenticated patron to return a previously borrowed book")
    public ResponseEntity<String> returnBook(@PathVariable UUID bookId) {
        UUID userId = getAuthenticatedUserId();
        logger.info("User {} is attempting to return book {}", userId, bookId);
        borrowService.returnBook(userId, bookId);
        logger.info("User {} successfully returned book {}", userId, bookId);
        return ResponseEntity.ok("Book returned successfully.");
    }

    @GetMapping("/history")
    @PreAuthorize("hasRole('ROLE_PATRON')")
    @Operation(summary = "Get authenticated user's borrow history",
            description = "Retrieves the borrow history of the currently authenticated patron")
    public ResponseEntity<List<BorrowResponse>> getOwnHistory() {
        UUID userId = getAuthenticatedUserId();
        logger.info("Fetching borrowing history for user {}", userId);
        List<BorrowResponse> history = borrowService.getUserBorrowingHistory(userId);
        logger.debug("Borrowing history size for user {}: {}", userId, history.size());
        return ResponseEntity.ok(history);
    }

    @GetMapping("/history/{userId}")
    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    @Operation(summary = "Get a specific user's borrow history",
            description = "Allows a librarian to retrieve the borrow history of a specific user by user ID")
    public ResponseEntity<List<BorrowResponse>> getUserHistory(@PathVariable UUID userId) {
        logger.info("Librarian requested borrowing history for user {}", userId);
        List<BorrowResponse> history = borrowService.getUserBorrowingHistory(userId);
        logger.debug("Borrowing history size for user {}: {}", userId, history.size());
        return ResponseEntity.ok(history);
    }

    @GetMapping("/overdue")
    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    @Operation(summary = "Get all overdue borrowed books",
            description = "Allows a librarian to retrieve a list of all overdue borrowed books")
    public ResponseEntity<List<BorrowResponse>> getOverdueBooks() {
        logger.info("Fetching all overdue borrowed books");
        List<BorrowResponse> overdue = borrowService.getOverdueBooks();
        logger.debug("Overdue books count: {}", overdue.size());
        return ResponseEntity.ok(overdue);
    }
}
