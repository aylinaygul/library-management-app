package com.aylinaygul.librarymanagementapp.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aylinaygul.librarymanagementapp.model.entity.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, UUID> {

}
