package com.aylinaygul.librarymanagementapp.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    private final BookRepository bookRepository;
    private final BookRequestMapper bookRequestMapper;
    private final BookResponseMapper bookResponseMapper;

    @Override
    public List<BookResponse> getAllBooks() {
        return bookResponseMapper.toDTOList(bookRepository.findAll());
    }

    @Override
    public BookResponse getBookById(UUID id) {
        return bookRepository.findById(id).map(bookResponseMapper::toDTO).orElse(null);
    }

    @Override
    public Page<BookResponse> searchBooks(String title, String author, String isbn, String genre,
            int page, int size) {
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

        return bookPage.map(bookResponseMapper::toDTO);
    }

    @Override
    @Transactional
    public BookResponse createBook(BookRequest bookRequest) {
        Book book = bookRequestMapper.toEntity(bookRequest);
        Book savedBook = bookRepository.save(book);
        return bookResponseMapper.toDTO(savedBook);
    }

    @Override
    @Transactional
    public BookResponse updateBook(UUID id, BookRequest bookRequest) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Task not found with ID: " + id));
        bookRequestMapper.updateBookFromRequest(bookRequest, book);
        return bookResponseMapper.toDTO(book);
    }

    @Override
    @Transactional
    public void deleteBook(UUID id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Task not found with ID: " + id));
        bookRepository.delete(book);
    }
}
