package com.aylinaygul.librarymanagementapp.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/books")
@Tag(name = "Books", description = "API endpoints for book CRUD operations")
public class BookController {

    private final BookService bookService;

    @Operation(summary = "Get all books",
            description = "Retrieves a list of all books in the system",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "List of books retrieved successfully",
                            content = @Content(mediaType = "application/json", array = @ArraySchema(
                                    schema = @Schema(implementation = BookResponse.class))))})
    @GetMapping()
    public ResponseEntity<List<BookResponse>> getAllBooks() {
        return ResponseEntity.status(HttpStatus.OK).body(bookService.getAllBooks());
    }

    @Operation(summary = "Get book by ID", description = "Retrieves a book by its unique ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Book found",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = BookResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Book not found")})
    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> getBookById(@PathVariable UUID id) {
        BookResponse book = bookService.getBookById(id);

        if (book == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(book);
    }

    @Operation(summary = "Search books",
            description = "Searches for books by title, author, ISBN, or genre with pagination",
            responses = {@ApiResponse(responseCode = "200",
                    description = "Books matching criteria retrieved",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BookResponse.class)))})
    @GetMapping("/search")
    public ResponseEntity<Page<BookResponse>> searchBooks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String isbn,
            @RequestParam(required = false) String genre,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(bookService.searchBooks(title, author, isbn, genre, page, size));
    }

    @Operation(summary = "Create a new book",
            description = "Creates a new book. Only librarians can perform this operation.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Book created successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = BookResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input")})
    @PostMapping()
    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    public ResponseEntity<BookResponse> createBook(@Valid @RequestBody BookRequest book) {
        BookResponse createdBook = bookService.createBook(book);
        return new ResponseEntity<>(createdBook, HttpStatus.CREATED);
    }

    @Operation(summary = "Update book by ID",
            description = "Updates the details of an existing book. Only librarians can perform this operation.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Book updated successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = BookResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "404", description = "Book not found")})
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    public ResponseEntity<BookResponse> updateBook(@PathVariable UUID id,
            @Valid @RequestBody BookRequest bookRequestDTO) {

        BookResponse updatedBook = bookService.updateBook(id, bookRequestDTO);
        return ResponseEntity.ok(updatedBook);
    }

    @Operation(summary = "Delete book by ID",
            description = "Deletes a book by its unique ID. Only librarians can perform this operation.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Book deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Book not found")})
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    public ResponseEntity<Void> deleteBook(@PathVariable UUID id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}
