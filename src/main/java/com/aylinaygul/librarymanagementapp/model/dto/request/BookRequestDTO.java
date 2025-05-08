package com.aylinaygul.librarymanagementapp.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookRequestDTO {
    private String title;
    private String author;
    private String genre;
    private String isbn;
    private LocalDate publicationDate;
    private boolean available;
}
