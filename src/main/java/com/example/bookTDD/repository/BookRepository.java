package com.example.bookTDD.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.bookTDD.model.Book;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    Page<Book> findByAuthorId(Long authorId, Pageable pageable);
    Optional<Book> findByIdAndAuthorId(Long id, Long authorId);
}
