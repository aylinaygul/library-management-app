package com.aylinaygul.librarymanagementapp.controller;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import com.aylinaygul.librarymanagementapp.model.dto.response.BorrowResponse;
import com.aylinaygul.librarymanagementapp.model.entity.BorrowStatus;
import com.aylinaygul.librarymanagementapp.model.entity.User;
import com.aylinaygul.librarymanagementapp.service.BorrowService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@ExtendWith(MockitoExtension.class)
public class BorrowControllerTest {

    @Mock
    private BorrowService borrowService;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private BorrowController borrowController;

    private UUID userId;
    private UUID bookId;
    private User mockUser;

    void initSecurityContext() {
        userId = UUID.randomUUID();
        bookId = UUID.randomUUID();

        mockUser = mock(User.class);
        when(mockUser.getId()).thenReturn(userId);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(mockUser);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void shouldBorrowBook() {
        initSecurityContext();

        doNothing().when(borrowService).borrowBook(userId, bookId);

        ResponseEntity<String> result = borrowController.borrowBook(bookId);

        assertEquals(200, result.getStatusCodeValue());
        assertEquals("Book borrowed successfully.", result.getBody());
        verify(borrowService).borrowBook(userId, bookId);
    }

    @Test
    void shouldReturnBook() {
        initSecurityContext();

        doNothing().when(borrowService).returnBook(userId, bookId);

        ResponseEntity<String> result = borrowController.returnBook(bookId);

        assertEquals(200, result.getStatusCodeValue());
        assertEquals("Book returned successfully.", result.getBody());
        verify(borrowService).returnBook(userId, bookId);
    }

    @Test
    void shouldGetOwnHistory() {
        initSecurityContext();

        List<BorrowResponse> history = List.of(BorrowResponse.builder().id(UUID.randomUUID())
                .userId(userId).bookId(bookId).bookTitle("Test Book").borrowDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(7)).status(BorrowStatus.BORROWED).build());

        when(borrowService.getUserBorrowingHistory(userId)).thenReturn(history);

        ResponseEntity<List<BorrowResponse>> result = borrowController.getOwnHistory();

        assertEquals(200, result.getStatusCodeValue());
        assertEquals(history, result.getBody());
        verify(borrowService).getUserBorrowingHistory(userId);
    }

    @Test
    void shouldGetUserHistory() {
        UUID otherUserId = UUID.randomUUID();
        UUID someUserId = UUID.randomUUID();
        UUID someBookId = UUID.randomUUID();

        List<BorrowResponse> history = List.of(BorrowResponse.builder().id(UUID.randomUUID())
                .userId(someUserId).bookId(someBookId).bookTitle("Test Book")
                .borrowDate(LocalDate.now()).dueDate(LocalDate.now().plusDays(7))
                .status(BorrowStatus.BORROWED).build());

        when(borrowService.getUserBorrowingHistory(otherUserId)).thenReturn(history);

        ResponseEntity<List<BorrowResponse>> result = borrowController.getUserHistory(otherUserId);

        assertEquals(200, result.getStatusCodeValue());
        assertEquals(history, result.getBody());
        verify(borrowService).getUserBorrowingHistory(otherUserId);
    }

    @Test
    void shouldGetOverdueBooks() {
        List<BorrowResponse> overdue = List.of(BorrowResponse.builder().id(UUID.randomUUID())
                .userId(UUID.randomUUID()).bookId(UUID.randomUUID()).bookTitle("Some Book")
                .borrowDate(LocalDate.now()).dueDate(LocalDate.now().plusDays(7))
                .status(BorrowStatus.OVERDUE).build());

        when(borrowService.getOverdueBooks()).thenReturn(overdue);

        ResponseEntity<List<BorrowResponse>> result = borrowController.getOverdueBooks();

        assertEquals(200, result.getStatusCodeValue());
        assertEquals(overdue, result.getBody());
        verify(borrowService).getOverdueBooks();
    }
}
