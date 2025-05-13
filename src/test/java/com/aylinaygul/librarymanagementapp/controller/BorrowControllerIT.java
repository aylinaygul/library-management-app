package com.aylinaygul.librarymanagementapp.controller;

import com.aylinaygul.librarymanagementapp.model.entity.Book;
import com.aylinaygul.librarymanagementapp.model.entity.Role;
import com.aylinaygul.librarymanagementapp.model.entity.User;
import com.aylinaygul.librarymanagementapp.repository.BookRepository;
import com.aylinaygul.librarymanagementapp.repository.BorrowRepository;
import com.aylinaygul.librarymanagementapp.repository.UserRepository;
import com.aylinaygul.librarymanagementapp.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class BorrowControllerIT {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private JwtUtil jwtUtil;

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private BookRepository bookRepository;

        private String patronToken;
        private UUID bookId;

        @BeforeEach
        void setUp() {
                User patron = User.builder().name("patron").email("patron@example.com")
                                .password("password").role(Role.PATRON).build();
                userRepository.save(patron);

                UserDetails userDetails = userRepository.findByEmail("patron@example.com")
                                .orElseThrow(() -> new RuntimeException("User not found"));

                patronToken = "Bearer " + jwtUtil.generateToken(userDetails);

                bookId = bookRepository.save(Book.builder().title("Test Book").author("Author")
                                .genre("Fiction").isbn("1234567893333")
                                .publicationDate(LocalDate.of(2020, 1, 1)).available(true).build())
                                .getId();
        }

        @Test
        void shouldBorrowBookSuccessfully() throws Exception {
                mockMvc.perform(post("/api/v1/borrow/" + bookId)
                                .header("Authorization", patronToken)
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk());
        }

        @Test
        void shouldReturnBookSuccessfully() throws Exception {
                mockMvc.perform(post("/api/v1/borrow/" + bookId)
                                .header("Authorization", patronToken)
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk());

                mockMvc.perform(post("/api/v1/borrow/return/" + bookId)
                                .header("Authorization", patronToken)
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk());
        }
}
