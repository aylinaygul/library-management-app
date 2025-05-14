package com.aylinaygul.librarymanagementapp.controller;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.aylinaygul.librarymanagementapp.model.dto.request.BookRequest;
import com.aylinaygul.librarymanagementapp.model.dto.response.BookResponse;
import com.aylinaygul.librarymanagementapp.service.BookService;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/books")
@Tag(name = "Books", description = "API endpoints for book CRUD operations")
public class BookController {

    private static final Logger logger = LoggerFactory.getLogger(BookController.class);

    private final BookService bookService;

    @GetMapping()
    @Operation(summary = "Get all books",
            description = "Retrieves a list of all books in the system",
            responses = {@ApiResponse(responseCode = "200",
                    description = "List of books retrieved successfully",
                    content = @Content(mediaType = "application/json", array = @ArraySchema(
                            schema = @Schema(implementation = BookResponse.class))))})
    public ResponseEntity<List<BookResponse>> getAllBooks() {
        logger.info("Fetching all books");
        List<BookResponse> books = bookService.getAllBooks();
        logger.debug("Total books found: {}", books.size());
        return ResponseEntity.status(HttpStatus.OK).body(books);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get book by ID", description = "Retrieves a book by its unique ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Book found",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = BookResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Book not found")})
    public ResponseEntity<BookResponse> getBookById(@PathVariable UUID id) {
        logger.info("Fetching book with ID: {}", id);
        BookResponse book = bookService.getBookById(id);

        if (book == null) {
            logger.warn("Book with ID {} not found", id);
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(book);
    }

    @GetMapping("/reports/overdue")
    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    @Operation(summary = "Generate overdue book report",
            description = "Retrieves a list of books that are overdue. Only librarians can access this report.",
            responses = {@ApiResponse(responseCode = "200", description = "Overdue books retrieved",
                    content = @Content(mediaType = "application/json", array = @ArraySchema(
                            schema = @Schema(implementation = BookResponse.class))))})
    public ResponseEntity<List<BookResponse>> getOverdueBooksReport() {
        logger.info("Generating overdue books report");
        List<BookResponse> overdueBooks = bookService.getOverdueBooks();
        logger.debug("Total overdue books found: {}", overdueBooks.size());
        return ResponseEntity.ok(overdueBooks);
    }

    @GetMapping("/search")
    @Operation(summary = "Search books",
            description = "Searches for books by title, author, ISBN, or genre with pagination",
            responses = {@ApiResponse(responseCode = "200",
                    description = "Books matching criteria retrieved",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BookResponse.class)))})
    public ResponseEntity<Page<BookResponse>> searchBooks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String isbn,
            @RequestParam(required = false) String genre,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        logger.debug(
                "Searching books with filters - title: {}, author: {}, isbn: {}, genre: {}, page: {}, size: {}",
                title, author, isbn, genre, page, size);
        Page<BookResponse> results =
                bookService.searchBooks(title, author, isbn, genre, page, size);
        return ResponseEntity.ok(results);
    }

    @PostMapping()
    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    @Operation(summary = "Create a new book",
            description = "Creates a new book. Only librarians can perform this operation.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Book created successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = BookResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input")})
    public ResponseEntity<BookResponse> createBook(@Valid @RequestBody BookRequest book) {
        logger.info("Creating a new book: {}", book.getTitle());
        BookResponse createdBook = bookService.createBook(book);
        logger.info("Book created with ID: {}", createdBook.id());
        return new ResponseEntity<>(createdBook, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    @Operation(summary = "Update book by ID",
            description = "Updates the details of an existing book. Only librarians can perform this operation.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Book updated successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = BookResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "404", description = "Book not found")})
    public ResponseEntity<BookResponse> updateBook(@PathVariable UUID id,
            @Valid @RequestBody BookRequest bookRequestDTO) {
        logger.info("Updating book with ID: {}", id);
        BookResponse updatedBook = bookService.updateBook(id, bookRequestDTO);
        logger.info("Successfully updated book with ID: {}", id);
        return ResponseEntity.ok(updatedBook);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    @Operation(summary = "Delete book by ID",
            description = "Deletes a book by its unique ID. Only librarians can perform this operation.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Book deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Book not found")})
    public ResponseEntity<Void> deleteBook(@PathVariable UUID id) {
        logger.warn("Attempting to delete book with ID: {}", id);
        bookService.deleteBook(id);
        logger.info("Successfully deleted book with ID: {}", id);
        return ResponseEntity.noContent().build();
    }
}
