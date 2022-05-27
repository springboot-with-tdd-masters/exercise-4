package com.csv.bookscrudexercise3.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.csv.bookscrudexercise3.model.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

	Page<Book> findByAuthorId(Long id, Pageable pageable);
	Page<Book> findByTitleContaining(String title, Pageable pageable);
	Optional<Book> findByAuthorIdAndId(Long authorId, Long bookId);
	Optional<Book> findByAuthorIdAndTitle(Long authorId, String title);
}
