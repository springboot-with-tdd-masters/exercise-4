package com.java.master.exercise1.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.java.master.exercise1.model.Book;
import com.java.master.exercise1.model.BookRequest;

public interface BookService {
	
	Book createUpdateBook(Book book);

	List<Book> getAllBooks();

	Book deleteBookById(Long id);

	Book getBookById(Long id);
	
	Book createBook(Long id, Book book);
	
	Page<Book> getBooksByAuthorId(Long id, Pageable pageable);
	
	Book getBookByAuthorIdAndByBookId(Long authorId, Long bookId);
	
	Book updateBook(Long authorId, Long bookId, BookRequest book);
	
	void deleteBookById(Long authorId, Long bookId);
}
