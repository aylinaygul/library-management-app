package com.aylinaygul.librarymanagementapp.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aylinaygul.librarymanagementapp.model.dto.response.BorrowResponse;
import com.aylinaygul.librarymanagementapp.model.entity.Book;
import com.aylinaygul.librarymanagementapp.model.entity.BorrowRecord;
import com.aylinaygul.librarymanagementapp.model.entity.BorrowStatus;
import com.aylinaygul.librarymanagementapp.model.entity.User;
import com.aylinaygul.librarymanagementapp.model.exception.BookNotAvailableException;
import com.aylinaygul.librarymanagementapp.model.exception.BookNotFoundException;
import com.aylinaygul.librarymanagementapp.model.exception.UserNotFoundException;
import com.aylinaygul.librarymanagementapp.model.mapper.BorrowResponseMapper;
import com.aylinaygul.librarymanagementapp.repository.BookRepository;
import com.aylinaygul.librarymanagementapp.repository.BorrowRepository;
import com.aylinaygul.librarymanagementapp.repository.UserRepository;
import com.aylinaygul.librarymanagementapp.service.BorrowService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BorrowServiceImpl implements BorrowService {

    private static final Logger logger = LoggerFactory.getLogger(BorrowServiceImpl.class);

    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final BorrowRepository borrowRepository;
    private final BorrowResponseMapper borrowResponseMapper;

    private User validateAndGetUser(UUID userId) {
        logger.debug("Validating user with ID: {}", userId);
        return userRepository.findById(userId).orElseThrow(() -> {
            logger.warn("User not found with ID: {}", userId);
            return new UserNotFoundException("User not found with ID: " + userId);
        });
    }

    private Book validateAndGetBook(UUID bookId) {
        logger.debug("Validating book with ID: {}", bookId);
        return bookRepository.findById(bookId).orElseThrow(() -> {
            logger.warn("Book not found with ID: {}", bookId);
            return new BookNotFoundException("Book not found with ID: " + bookId);
        });
    }

    private void checkBookAvailability(Book book) {
        if (!book.isAvailable()) {
            logger.warn("Attempted to borrow unavailable book: {}", book.getTitle());
            throw new BookNotAvailableException("Book is not available for borrowing.");
        }
    }

    @Override
    @Transactional
    public void borrowBook(UUID userId, UUID bookId) {
        logger.info("User {} attempting to borrow book {}", userId, bookId);
        User user = validateAndGetUser(userId);
        Book book = validateAndGetBook(bookId);

        checkBookAvailability(book);

        BorrowRecord borrowRecord = BorrowRecord.builder().user(user).book(book)
                .borrowDate(LocalDate.now()).dueDate(LocalDate.now().plusDays(14))
                .status(BorrowStatus.BORROWED).build();

        borrowRepository.save(borrowRecord);
        logger.info("Borrow record created for user {} and book {}", userId, bookId);

        book.setAvailable(false);
        bookRepository.save(book);
        logger.info("Book {} marked as unavailable", bookId);
    }

    @Override
    @Transactional
    public void returnBook(UUID userId, UUID bookId) {
        logger.info("User {} attempting to return book {}", userId, bookId);

        BorrowRecord borrowRecord = borrowRepository
                .findByUserIdAndBookIdAndStatus(userId, bookId, BorrowStatus.BORROWED)
                .orElseThrow(() -> {
                    logger.error("No active borrow record found for user {} and book {}", userId,
                            bookId);
                    return new IllegalArgumentException("Active borrow record not found");
                });

        borrowRecord.setReturnDate(LocalDate.now());
        borrowRecord.setStatus(BorrowStatus.RETURNED);
        borrowRepository.save(borrowRecord);
        logger.info("Borrow record updated to RETURNED for user {} and book {}", userId, bookId);

        Book book = borrowRecord.getBook();
        book.setAvailable(true);
        bookRepository.save(book);
        logger.info("Book {} marked as available", book.getId());
    }

    @Override
    public List<BorrowResponse> getUserBorrowingHistory(UUID userId) {
        logger.info("Fetching borrowing history for user {}", userId);
        List<BorrowRecord> records = borrowRepository.findAllByUserIdOrderByBorrowDateDesc(userId);
        logger.debug("Total borrow records found: {}", records.size());
        return borrowResponseMapper.toDTOList(records);
    }

    @Override
    public List<BorrowResponse> getAllBorrowHistory() {
        logger.info("Fetching all borrowing history");
        List<BorrowRecord> records = borrowRepository.findAllByOrderByBorrowDateDesc();
        logger.debug("Total borrow records in system: {}", records.size());
        return borrowResponseMapper.toDTOList(records);
    }

    @Override
    public List<BorrowResponse> getOverdueBooks() {
        logger.info("Fetching overdue books");
        List<BorrowRecord> records = borrowRepository
                .findByStatusAndDueDateBefore(BorrowStatus.BORROWED, LocalDate.now());
        logger.debug("Total overdue records: {}", records.size());
        return borrowResponseMapper.toDTOList(records);
    }
}
