package com.example.exercisefour.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.exercisefour.model.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>{
	
	Page<Book> findByAuthorId(Long id, Pageable page);
	Page<Book> findByAuthorIdAndId(Long authorId, Long bookId, Pageable page);
	Page<Book> findByTitleContaining(String title, Pageable page);

}
