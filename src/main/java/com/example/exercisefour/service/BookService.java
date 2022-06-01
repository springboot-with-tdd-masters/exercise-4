package com.example.exercisefour.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.exercisefour.model.Author;
import com.example.exercisefour.model.Book;
import com.example.exercisefour.response.AuthorResponse;
import com.example.exercisefour.response.BookResponse;

public interface BookService {
	
	AuthorResponse createAuthor(Author authorRequest);
	Page<AuthorResponse> getAllAuthors(Pageable page);
	AuthorResponse getAuthorById(Long id);
	BookResponse createBook(Long id, Book book);
	Page<BookResponse> getAllBooks(Long id, Pageable page);
	Page<BookResponse> getBookById(Long authorId, Long bookId, Pageable page);
	Page<BookResponse> getBookByTitle(String title, Pageable page);
	BookResponse updateBook(Long authorId, Long bookId, Book book);
	Book deleteByBookId(Long authorId, Long bookId);
	
}
