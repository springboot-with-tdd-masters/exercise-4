package com.example.basicauth.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.basicauth.model.dto.BookDto;
import com.example.basicauth.model.dto.BookRequest;

public interface BookService {
	
	BookDto addBook(Long id, BookRequest request);
	Page<BookDto> getBook(Long bookId, Long authorId, Pageable pageable);
	Page<BookDto> getBooks(String string);
	Page<BookDto> getBooks(Long authorId, Pageable pageable);
}
