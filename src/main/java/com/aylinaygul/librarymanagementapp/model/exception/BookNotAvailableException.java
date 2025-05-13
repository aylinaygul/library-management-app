package com.aylinaygul.librarymanagementapp.model.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

public class BookNotAvailableException extends RuntimeException {
    private static final Logger log = LoggerFactory.getLogger(BookNotAvailableException.class);

    public BookNotAvailableException(String message) {
        super(message);
    }

    @ExceptionHandler(BookNotAvailableException.class)
    public ResponseEntity<Map<String, String>> handleBookNotAvailable(
            BookNotAvailableException ex) {
        log.error("Error occurred: {}", ex.getMessage());

        Map<String, String> response = new HashMap<>();
        response.put("error", ex.getMessage());
        response.put("errorCode", "BOOK_NOT_AVAILABLE");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }
}
