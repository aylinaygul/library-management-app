package com.aylinaygul.librarymanagementapp.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.aylinaygul.librarymanagementapp.model.dto.request.BookRequest;
import com.aylinaygul.librarymanagementapp.model.dto.response.BookResponse;
import com.aylinaygul.librarymanagementapp.service.BookService;

@ExtendWith(MockitoExtension.class)
public class BookControllerTest {

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    @Test
    void shouldGetAllBooks() {
        BookResponse bookResponse = new BookResponse(UUID.randomUUID(), "Test Book Title",
                "Test Author", "Fiction", "1234567890", LocalDate.of(2020, 1, 1), true, null);

        List<BookResponse> bookResponseList = Collections.singletonList(bookResponse);

        when(bookService.getAllBooks()).thenReturn(bookResponseList);

        ResponseEntity<List<BookResponse>> response = bookController.getAllBooks();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody().isEmpty());
        assertEquals(bookResponseList, response.getBody());
    }

    @Test
    void shouldGetBookById() {
        UUID bookId = UUID.randomUUID();
        BookResponse bookResponse = new BookResponse(bookId, "Test Book Title", "Test Author",
                "Fiction", "1234567890", LocalDate.of(2020, 1, 1), true, null);

        when(bookService.getBookById(bookId)).thenReturn(bookResponse);

        ResponseEntity<BookResponse> response = bookController.getBookById(bookId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(bookResponse, response.getBody());
    }

    @Test
    void shouldReturnNotFoundWhenBookDoesNotExist() {
        UUID bookId = UUID.randomUUID();

        when(bookService.getBookById(bookId)).thenReturn(null);

        ResponseEntity<BookResponse> response = bookController.getBookById(bookId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void shouldCreateBook() {
        BookRequest bookRequest = new BookRequest("Test Book Title", "Test Author", "Fiction",
                "1234567890", LocalDate.of(2020, 1, 1), true);

        BookResponse bookResponse = new BookResponse(UUID.randomUUID(), "Test Book Title",
                "Test Author", "Fiction", "1234567890", LocalDate.of(2020, 1, 1), true, null);

        when(bookService.createBook(bookRequest)).thenReturn(bookResponse);

        ResponseEntity<BookResponse> response = bookController.createBook(bookRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(bookResponse, response.getBody());
    }

    @Test
    void shouldUpdateBook() {
        UUID bookId = UUID.randomUUID();
        BookRequest bookRequest = new BookRequest("Updated Book Title", "Updated Author",
                "Non-Fiction", "0987654321", LocalDate.of(2021, 1, 1), false);

        BookResponse bookResponse = new BookResponse(bookId, "Updated Book Title", "Updated Author",
                "Non-Fiction", "0987654321", LocalDate.of(2021, 1, 1), false, null);

        when(bookService.updateBook(bookId, bookRequest)).thenReturn(bookResponse);

        ResponseEntity<BookResponse> response = bookController.updateBook(bookId, bookRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(bookResponse, response.getBody());
    }

    @Test
    void shouldDeleteBook() {
        UUID bookId = UUID.randomUUID();

        ResponseEntity<Void> response = bookController.deleteBook(bookId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void shouldSearchBooks() {
        String title = "Test Book Title";
        String author = "Test Author";
        String isbn = "1234567890";
        String genre = "Fiction";
        int page = 0;
        int size = 10;

        BookResponse bookResponse = new BookResponse(UUID.randomUUID(), title, author, genre, isbn,
                LocalDate.of(2020, 1, 1), true, null);

        List<BookResponse> bookResponseList = Collections.singletonList(bookResponse);
        Page<BookResponse> pageResult = new PageImpl<>(bookResponseList);

        when(bookService.searchBooks(title, author, isbn, genre, page, size))
                .thenReturn(pageResult);

        ResponseEntity<Page<BookResponse>> response =
                bookController.searchBooks(title, author, isbn, genre, page, size);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody().isEmpty());
        assertEquals(bookResponseList, response.getBody().getContent());
    }

}
