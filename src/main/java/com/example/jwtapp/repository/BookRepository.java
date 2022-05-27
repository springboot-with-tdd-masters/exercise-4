package com.example.jwtapp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.jwtapp.model.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>{

	Page<Book> findByIdAndAuthorId(Long id, Long authorId, Pageable pageable);
	Page<Book> findByTitleContains(String title, Pageable pageable);
	Page<Book> findByAuthorId(Long id, Pageable pageable);
}
