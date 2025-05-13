package com.aylinaygul.librarymanagementapp.service;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;

import com.aylinaygul.librarymanagementapp.model.dto.request.BookRequest;
import com.aylinaygul.librarymanagementapp.model.dto.response.BookResponse;

public interface BookService {

    List<BookResponse> getAllBooks();

    BookResponse getBookById(UUID id);

    Page<BookResponse> searchBooks(String title, String author, String isbn, String genre, int page,
            int size);

    BookResponse createBook(BookRequest bookRequest);

    BookResponse updateBook(UUID id, BookRequest bookRequest);

    void deleteBook(UUID id);
}
