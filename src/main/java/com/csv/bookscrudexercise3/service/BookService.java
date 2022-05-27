package com.csv.bookscrudexercise3.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.csv.bookscrudexercise3.exception.RecordNotFoundException;
import com.csv.bookscrudexercise3.model.Book;
import com.csv.bookscrudexercise3.model.BookUpdate;

@Service
public interface BookService {

	public Page<Book> getBooksByAuthorId(Long authorId, Pageable pageable) throws RecordNotFoundException;
	
	public Page<Book> getBooksByTitleContaining(String title, Pageable pageable);
	
	public Book getBookByAuthorIdAndByBookId(Long authorId, Long bookId) throws RecordNotFoundException;

	public Book createBook(Long authorId, Book bookEntity) throws RecordNotFoundException;
	
	public Book updateBook(BookUpdate newBook, Book book);
	
	public void deleteBookById(Long authorId, Long bookId) throws RecordNotFoundException;
}
