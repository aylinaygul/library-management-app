package com.aylinaygul.librarymanagementapp.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.aylinaygul.librarymanagementapp.model.entity.Book;

public interface BookRepository extends JpaRepository<Book, UUID> {

    @Query("""
            SELECT b FROM Book b WHERE b.id IN (
                SELECT br.book.id FROM BorrowRecord br WHERE br.dueDate < CURRENT_DATE AND br.returnDate IS NULL
            )
            """)
    List<Book> findOverdueBooks();
}
