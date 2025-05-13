package com.aylinaygul.librarymanagementapp.service.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.aylinaygul.librarymanagementapp.model.dto.request.BookRequest;
import com.aylinaygul.librarymanagementapp.model.dto.response.BookResponse;
import com.aylinaygul.librarymanagementapp.model.entity.Book;
import com.aylinaygul.librarymanagementapp.model.mapper.BookRequestMapper;
import com.aylinaygul.librarymanagementapp.model.mapper.BookResponseMapper;
import com.aylinaygul.librarymanagementapp.repository.BookRepository;

@ExtendWith(MockitoExtension.class)
public class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookRequestMapper bookRequestMapper;

    @Mock
    private BookResponseMapper bookResponseMapper;

    @InjectMocks
    private BookServiceImpl bookService;

    private BookRequest bookRequest;
    private Book book;
    private BookResponse bookResponse;

    @BeforeEach
    public void setUp() {
        bookRequest = new BookRequest("Test Book Title", "Test Author", "Fiction", "1234567890",
                LocalDate.of(2020, 1, 1), true);

        book = new Book(UUID.randomUUID(), "Test Book Title", "Test Author", "Fiction",
                "1234567890", LocalDate.of(2020, 1, 1), true, null);

        bookResponse = new BookResponse(book.getId(), book.getTitle(), book.getAuthor(),
                book.getGenre(), book.getIsbn(), book.getPublicationDate(), book.isAvailable(),
                book.getRecords());
    }

    @Test
    public void testCreateBook() {
        when(bookRequestMapper.toEntity(bookRequest)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);
        when(bookResponseMapper.toDTO(book)).thenReturn(bookResponse);

        BookResponse result = bookService.createBook(bookRequest);

        assertNotNull(result);
        assertEquals(bookResponse, result);
        verify(bookRepository).save(book);
        verify(bookRequestMapper).toEntity(bookRequest);
        verify(bookResponseMapper).toDTO(book);
    }

    @Test
    public void testGetAllBooks() {
        List<Book> books = List.of(book);
        List<BookResponse> bookResponses = List.of(bookResponse);
        when(bookRepository.findAll()).thenReturn(books);
        when(bookResponseMapper.toDTOList(books)).thenReturn(bookResponses);

        List<BookResponse> result = bookService.getAllBooks();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(bookResponse, result.get(0));
        verify(bookRepository).findAll();
        verify(bookResponseMapper).toDTOList(books);
    }

    @Test
    public void testDeleteBook() {
        UUID bookId = UUID.randomUUID();
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        bookService.deleteBook(bookId);

        verify(bookRepository).findById(bookId);
        verify(bookRepository).delete(book);
    }

    @Test
    public void testSearchBooks() {
        String title = "Test Book Title";
        String author = "Test Author";
        String isbn = "1234567890";
        String genre = "Fiction";
        int page = 0;
        int size = 10;

        Pageable pageable = Pageable.ofSize(size).withPage(page);
        Book probe = new Book();
        probe.setTitle(title);
        probe.setAuthor(author);
        probe.setIsbn(isbn);
        probe.setGenre(genre);

        Example<Book> example = Example.of(probe);
        Page<Book> bookPage = new PageImpl<>(List.of(book), pageable, 1);

        when(bookRepository.findAll(any(Example.class), any(Pageable.class))).thenReturn(bookPage);
        when(bookResponseMapper.toDTO(book)).thenReturn(bookResponse);

        Page<BookResponse> result = bookService.searchBooks(title, author, isbn, genre, page, size);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(bookResponse, result.getContent().get(0));

        verify(bookRepository).findAll(any(Example.class), any(Pageable.class));
        verify(bookResponseMapper).toDTO(book);
    }

    @Test
    public void testGetBookById_BookFound() {
        UUID bookId = UUID.randomUUID();
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(bookResponseMapper.toDTO(book)).thenReturn(bookResponse);

        BookResponse result = bookService.getBookById(bookId);

        assertNotNull(result);
        assertEquals(bookResponse, result);
        verify(bookRepository).findById(bookId);
        verify(bookResponseMapper).toDTO(book);
    }

    @Test
    public void testGetBookById_BookNotFound() {
        UUID bookId = UUID.randomUUID();
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        BookResponse result = bookService.getBookById(bookId);

        assertNull(result);
        verify(bookRepository).findById(bookId);
    }
}
