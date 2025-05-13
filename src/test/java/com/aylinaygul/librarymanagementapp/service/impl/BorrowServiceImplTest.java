package com.aylinaygul.librarymanagementapp.service.impl;

import com.aylinaygul.librarymanagementapp.model.entity.*;
import com.aylinaygul.librarymanagementapp.model.dto.response.BorrowResponse;
import com.aylinaygul.librarymanagementapp.model.mapper.BorrowResponseMapper;
import com.aylinaygul.librarymanagementapp.repository.BookRepository;
import com.aylinaygul.librarymanagementapp.repository.BorrowRepository;
import com.aylinaygul.librarymanagementapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BorrowServiceImplTest {

    @InjectMocks
    private BorrowServiceImpl borrowService;

    @Mock
    private BorrowRepository borrowRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BorrowResponseMapper borrowResponseMapper;

    private UUID userId;
    private UUID bookId;
    private User user;
    private Book book;
    private BorrowRecord borrowRecord;

    @BeforeEach
    void setUp() {
        userId = UUID.fromString("00000000-0000-0000-0000-000000000001");
        bookId = UUID.fromString("00000000-0000-0000-0000-000000000002");

        user = User.builder().id(userId).name("Test User").email("test@example.com")
                .password("password").role(Role.PATRON).build();

        book = Book.builder().id(bookId).title("Test Book").available(true) // 👈 Bu satır çok
                                                                            // kritik!
                .build();

        borrowRecord = BorrowRecord.builder().id(UUID.randomUUID()).user(user).book(book)
                .status(BorrowStatus.BORROWED).borrowDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(14)).build();
    }

    @Test
    void borrowBook_ShouldSaveBorrowRecord() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));


        when(borrowRepository.save(any(BorrowRecord.class))).thenAnswer(i -> i.getArguments()[0]);

        borrowService.borrowBook(userId, bookId);

        verify(borrowRepository, times(1)).save(any(BorrowRecord.class));
    }


    @Test
    void returnBook_ShouldUpdateReturnDateAndStatus() {
        borrowRecord.setReturnDate(null);
        when(borrowRepository.findByUserIdAndBookIdAndStatus(eq(userId), eq(bookId),
                eq(BorrowStatus.BORROWED))).thenReturn(Optional.of(borrowRecord));

        borrowService.returnBook(userId, bookId);

        assertEquals(BorrowStatus.RETURNED, borrowRecord.getStatus());
        assertNotNull(borrowRecord.getReturnDate());
    }

    @Test
    void getUserBorrowingHistory_ShouldReturnListOfBorrowResponse() {
        List<BorrowRecord> records = List.of(borrowRecord);
        when(borrowRepository.findAllByUserIdOrderByBorrowDateDesc(eq(userId))).thenReturn(records);

        BorrowResponse response = new BorrowResponse();
        response.setBookId(bookId);
        response.setUserId(userId);
        response.setDueDate(borrowRecord.getDueDate());
        when(borrowResponseMapper.toDTOList(records)).thenReturn(List.of(response));

        List<BorrowResponse> responses = borrowService.getUserBorrowingHistory(userId);

        assertEquals(1, responses.size());
        assertEquals(bookId, responses.get(0).getBookId());
        assertEquals(userId, responses.get(0).getUserId());
    }

    @Test
    void getOverdueBooks_ShouldReturnOnlyOverdueRecords() {
        BorrowRecord overdueRecord = BorrowRecord.builder().id(UUID.randomUUID()).book(book)
                .user(user).status(BorrowStatus.BORROWED).borrowDate(LocalDate.now().minusDays(20))
                .dueDate(LocalDate.now().minusDays(5)).returnDate(null).build();

        when(borrowRepository.findByStatusAndDueDateBefore(eq(BorrowStatus.BORROWED),
                eq(LocalDate.now()))).thenReturn(List.of(overdueRecord));

        BorrowResponse response = new BorrowResponse();
        response.setBookId(bookId);
        response.setUserId(userId);
        response.setDueDate(overdueRecord.getDueDate());
        when(borrowResponseMapper.toDTOList(List.of(overdueRecord))).thenReturn(List.of(response));

        List<BorrowResponse> overdueBooks = borrowService.getOverdueBooks();

        assertEquals(1, overdueBooks.size());
        assertTrue(overdueBooks.get(0).getDueDate().isBefore(LocalDate.now()));
    }
}
