package com.aylinaygul.librarymanagementapp.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.aylinaygul.librarymanagementapp.model.dto.request.BookRequest;
import com.aylinaygul.librarymanagementapp.model.dto.response.BookResponse;
import com.aylinaygul.librarymanagementapp.model.entity.Book;
import com.aylinaygul.librarymanagementapp.model.mapper.BookRequestMapper;
import com.aylinaygul.librarymanagementapp.model.mapper.BookResponseMapper;
import com.aylinaygul.librarymanagementapp.repository.BookRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final BookRequestMapper bookRequestMapper;
    private final BookResponseMapper bookResponseMapper;

    public List<BookResponse> getAllBooks() {
        return bookResponseMapper.toDTOList(bookRepository.findAll());
    }

    public BookResponse getBookById(UUID id) {
        return bookRepository.findById(id)
                .map(bookResponseMapper::toDTO)
                .orElse(null);
    }

    @Transactional
    public BookResponse createBook(BookRequest bookRequest) {
        Book book = bookRequestMapper.toEntity(bookRequest);
        Book savedBook = bookRepository.save(book);
        return bookResponseMapper.toDTO(savedBook);
    }

    @Transactional
    public BookResponse updateBook(UUID id, BookRequest bookRequest) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Task not found with ID: " + id));
        bookRequestMapper.updateBookFromRequest(bookRequest, book);
        return bookResponseMapper.toDTO(book);
    }

    @Transactional
    public void deleteBook(UUID id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Task not found with ID: " + id));
        bookRepository.delete(book);
    }
}
