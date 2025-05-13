package com.aylinaygul.librarymanagementapp.controller;

import com.aylinaygul.librarymanagementapp.model.entity.Book;
import com.aylinaygul.librarymanagementapp.repository.BookRepository;

import jakarta.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@WithMockUser(roles = "LIBRARIAN")
public class BookControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookRepository bookRepository;

    @BeforeEach
    void setup() {
        bookRepository.deleteAll();

        Book book = new Book();
        book.setTitle("Test Book");
        book.setAuthor("Test Author");
        book.setGenre("Fiction");
        book.setIsbn("1234567890");
        book.setPublicationDate(LocalDate.of(2020, 1, 1));
        book.setAvailable(true);
        bookRepository.save(book);
    }

    @Test
    void shouldGetAllBooks() throws Exception {
        mockMvc.perform(get("/api/v1/books").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", is("Test Book")));
    }

    @Test
    void shouldGetBookById() throws Exception {
        UUID id = bookRepository.findAll().get(0).getId();

        mockMvc.perform(get("/api/v1/books/{id}", id).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(jsonPath("$.title", is("Test Book")));
    }

    @Test
    void shouldReturnNotFoundForNonExistentBook() throws Exception {
        mockMvc.perform(get("/api/v1/books/{id}", UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateBook() throws Exception {
        String bookJson = """
                {
                "title": "New Book",
                "author": "New Author",
                "genre": "Fiction",
                "isbn": "1112223339",
                "publicationDate": "2022-01-01",
                "available": true
                }
                """;

        mockMvc.perform(
                post("/api/v1/books").contentType(MediaType.APPLICATION_JSON).content(bookJson))
                .andExpect(status().isCreated()).andExpect(jsonPath("$.title", is("New Book")));
    }

    @Test
    void shouldDeleteBook() throws Exception {
        UUID id = bookRepository.findAll().get(0).getId();

        mockMvc.perform(delete("/api/v1/books/{id}", id)).andExpect(status().isNoContent());
    }
}
