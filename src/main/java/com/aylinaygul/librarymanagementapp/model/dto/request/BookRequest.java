package com.aylinaygul.librarymanagementapp.model.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookRequest {

    @NotBlank(message = "Title is required.")
    private String title;

    @NotBlank(message = "Author is required.")
    private String author;

    @NotBlank(message = "Genre is required.")
    private String genre;

    @NotBlank(message = "ISBN is required.")
    @Pattern(regexp = "\\d{10}|\\d{13}", message = "ISBN must be 10 or 13 digits.")
    private String isbn;

    @NotNull(message = "Publication date is required.")
    @PastOrPresent(message = "Publication date cannot be in the future.")
    private LocalDate publicationDate;

    private boolean available;
}
