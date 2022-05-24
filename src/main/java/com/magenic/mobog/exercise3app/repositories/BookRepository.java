package com.magenic.mobog.exercise3app.repositories;

import com.magenic.mobog.exercise3app.entities.Author;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.magenic.mobog.exercise3app.entities.Book;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findAll();
    Page<Book> findByAuthorId(Long authorId, Pageable page);
    Optional<Book> findByIdAndAuthorId(Long id, Long authorId);
    Page<Book> findAll(Pageable page);
    Page<Book> findByTitleContaining(String title, Pageable page);
}
