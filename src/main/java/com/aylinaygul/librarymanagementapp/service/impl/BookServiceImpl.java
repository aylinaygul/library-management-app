package com.aylinaygul.librarymanagementapp.service.impl;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import com.aylinaygul.librarymanagementapp.model.dto.request.BookRequest;
import com.aylinaygul.librarymanagementapp.model.dto.response.BookResponse;
import com.aylinaygul.librarymanagementapp.model.entity.Book;
import com.aylinaygul.librarymanagementapp.model.mapper.BookRequestMapper;
import com.aylinaygul.librarymanagementapp.model.mapper.BookResponseMapper;
import com.aylinaygul.librarymanagementapp.repository.BookRepository;
import com.aylinaygul.librarymanagementapp.service.BookService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private static final Logger logger = LoggerFactory.getLogger(BookServiceImpl.class);

    private final BookRepository bookRepository;
    private final BookRequestMapper bookRequestMapper;
    private final BookResponseMapper bookResponseMapper;

    @Override
    public List<BookResponse> getAllBooks() {
        logger.info("Fetching all books from the database.");
        List<BookResponse> books = bookResponseMapper.toDTOList(bookRepository.findAll());
        logger.debug("Total books fetched: {}", books.size());
        return books;
    }

    @Override
    public BookResponse getBookById(UUID id) {
        logger.info("Fetching book with ID: {}", id);
        return bookRepository.findById(id).map(book -> {
            logger.debug("Book found: {}", book.getTitle());
            return bookResponseMapper.toDTO(book);
        }).orElseGet(() -> {
            logger.warn("Book not found with ID: {}", id);
            return null;
        });
    }

    @Override
    public List<BookResponse> getOverdueBooks() {
        logger.info("Fetching overdue books from the database.");
        List<Book> overdueBooks = bookRepository.findOverdueBooks();
        return bookResponseMapper.toDTOList(overdueBooks);
    }


    @Override
    public Page<BookResponse> searchBooks(String title, String author, String isbn, String genre,
            int page, int size) {
        logger.info("Searching books with parameters - Title: {}, Author: {}, ISBN: {}, Genre: {}",
                title, author, isbn, genre);

        Book probe = new Book();
        probe.setTitle(title);
        probe.setAuthor(author);
        probe.setIsbn(isbn);
        probe.setGenre(genre);

        ExampleMatcher matcher = ExampleMatcher.matchingAll().withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);

        Example<Book> example = Example.of(probe, matcher);
        Pageable pageable = PageRequest.of(page, size);

        Page<Book> bookPage = bookRepository.findAll(example, pageable);
        logger.debug("Books found: {}", bookPage.getTotalElements());

        return bookPage.map(bookResponseMapper::toDTO);
    }

    @Override
    @Transactional
    public BookResponse createBook(BookRequest bookRequest) {
        logger.info("Creating new book: {}", bookRequest.getTitle());
        Book book = bookRequestMapper.toEntity(bookRequest);
        Book savedBook = bookRepository.save(book);
        logger.info("Book created successfully with ID: {}", savedBook.getId());
        return bookResponseMapper.toDTO(savedBook);
    }

    @Override
    @Transactional
    public BookResponse updateBook(UUID id, BookRequest bookRequest) {
        logger.info("Updating book with ID: {}", id);

        Book book = bookRepository.findById(id).orElseThrow(() -> {
            logger.error("Book not found with ID: {}", id);
            return new IllegalArgumentException("Book not found with ID: " + id);
        });

        bookRequestMapper.updateBookFromRequest(bookRequest, book);
        logger.info("Book updated successfully: {}", id);
        return bookResponseMapper.toDTO(book);
    }

    @Override
    @Transactional
    public void deleteBook(UUID id) {
        logger.info("Deleting book with ID: {}", id);

        Book book = bookRepository.findById(id).orElseThrow(() -> {
            logger.error("Book not found with ID: {}", id);
            return new IllegalArgumentException("Book not found with ID: " + id);
        });

        bookRepository.delete(book);
        logger.info("Book deleted successfully: {}", id);
    }
}
