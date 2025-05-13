package com.aylinaygul.librarymanagementapp.model.exception;

public class BookNotFoundException extends RuntimeException {
    private final String errorCode = "BOOK_NOT_FOUND";

    public BookNotFoundException(String message) {
        super(message);
    }

    public String getErrorCode() {
        return errorCode;
    }
}
