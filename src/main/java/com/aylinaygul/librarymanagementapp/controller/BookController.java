package com.aylinaygul.librarymanagementapp.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aylinaygul.librarymanagementapp.model.dto.request.BookRequestDTO;
import com.aylinaygul.librarymanagementapp.model.dto.response.BookResponseDTO;
import com.aylinaygul.librarymanagementapp.service.BookService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/books")
public class BookController {
    private final BookService bookService;

    @GetMapping()
    public ResponseEntity<List<BookResponseDTO>> getAllBooks() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(bookService.getAllBooks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponseDTO> getBookById(@PathVariable UUID id) {
        BookResponseDTO book = bookService.getBookById(id);

        if (book == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(book);
    }

    @PostMapping()
    public ResponseEntity<BookResponseDTO> createBook(@RequestBody BookRequestDTO book) {
        BookResponseDTO createdBook = bookService.createBook(book);
        return new ResponseEntity<>(createdBook, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookResponseDTO> updateBook(
            @PathVariable UUID id,
            @RequestBody BookRequestDTO bookRequestDTO) {

        BookResponseDTO updatedBook = bookService.updateBook(id, bookRequestDTO);
        return ResponseEntity.ok(updatedBook);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable UUID id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

}
