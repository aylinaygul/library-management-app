package com.aylinaygul.librarymanagementapp.model.exception;

public class BookNotAvailableException extends RuntimeException {
    public BookNotAvailableException(String message) {
        super(message);
    }
}