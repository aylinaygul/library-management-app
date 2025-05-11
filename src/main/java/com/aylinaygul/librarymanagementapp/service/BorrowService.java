package com.aylinaygul.librarymanagementapp.service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aylinaygul.librarymanagementapp.model.exception.BookNotFoundException;
import com.aylinaygul.librarymanagementapp.model.exception.BookNotAvailableException;
import com.aylinaygul.librarymanagementapp.model.exception.UserNotFoundException;
import com.aylinaygul.librarymanagementapp.model.dto.response.BorrowResponse;
import com.aylinaygul.librarymanagementapp.model.entity.Book;
import com.aylinaygul.librarymanagementapp.model.entity.BorrowRecord;
import com.aylinaygul.librarymanagementapp.model.entity.BorrowStatus;
import com.aylinaygul.librarymanagementapp.model.entity.User;
import com.aylinaygul.librarymanagementapp.model.mapper.BorrowResponseMapper;
import com.aylinaygul.librarymanagementapp.repository.BookRepository;
import com.aylinaygul.librarymanagementapp.repository.BorrowRepository;
import com.aylinaygul.librarymanagementapp.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BorrowService {

    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final BorrowRepository borrowRepository;
    private final BorrowResponseMapper borrowResponseMapper;

    private User validateAndGetUser(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));
    }

    private Book validateAndGetBook(UUID bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book not found with ID: " + bookId));
    }

    private void checkBookAvailability(Book book) {
        if (!book.isAvailable()) {
            throw new BookNotAvailableException("Book is not available for borrowing.");
        }
    }

    @Transactional
    public void borrowBook(UUID userId, UUID bookId) {
        User user = validateAndGetUser(userId);
        Book book = validateAndGetBook(bookId);

        checkBookAvailability(book);

        BorrowRecord borrowRecord = BorrowRecord.builder()
                .user(user)
                .book(book)
                .borrowDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(14))
                .status(BorrowStatus.BORROWED)
                .build();

        borrowRepository.save(borrowRecord);

        book.setAvailable(false);
        bookRepository.save(book);
    }

    @Transactional
    public void returnBook(UUID userId, UUID bookId) {
        BorrowRecord borrowRecord = borrowRepository
                .findByUserIdAndBookIdAndStatus(userId, bookId, BorrowStatus.BORROWED)
                .orElseThrow(() -> new IllegalArgumentException("Active borrow record not found"));

        borrowRecord.setReturnDate(LocalDate.now());
        borrowRecord.setStatus(BorrowStatus.RETURNED);
        borrowRepository.save(borrowRecord);

        Book book = borrowRecord.getBook();
        book.setAvailable(true);
        bookRepository.save(book);
    }

    public List<BorrowResponse> getUserBorrowingHistory(UUID userId) {
        List<BorrowRecord> records = borrowRepository.findAllByUserIdOrderByBorrowDateDesc(userId);
        return borrowResponseMapper.toDTOList(records);
    }

    public List<BorrowResponse> getAllBorrowHistory() {
        List<BorrowRecord> records = borrowRepository.findAllByOrderByBorrowDateDesc();
        return borrowResponseMapper.toDTOList(records);
    }

    public List<BorrowResponse> getOverdueBooks() {
        List<BorrowRecord> records = borrowRepository.findByStatusAndDueDateBefore(
                BorrowStatus.BORROWED, LocalDate.now());
        return borrowResponseMapper.toDTOList(records);
    }
}
