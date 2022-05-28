package com.example.basicauth.service;

import javax.security.auth.login.AccountNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.basicauth.exception.AuthorNotFoundException;
import com.example.basicauth.model.Author;
import com.example.basicauth.model.Book;
import com.example.basicauth.model.dto.AuthorDto;
import com.example.basicauth.model.dto.BookDto;
import com.example.basicauth.model.dto.BookRequest;
import com.example.basicauth.repository.AuthorRepository;
import com.example.basicauth.repository.BookRepository;

@Service
public class BookServiceImpl implements BookService {
	
	@Autowired
	private BookRepository bookRepository;
	
	@Autowired
	private AuthorRepository authorRepository;
	
	public BookDto addBook(Long id, BookRequest request) {
		Author author = authorRepository.findById(id).orElseThrow(AuthorNotFoundException::new);
		
		Book book = new Book();
		book.setTitle(request.getTitle());
		book.setDescription(request.getDescription());
		book.setAuthor(author);

		Book savedBook = bookRepository.save(book);
		
		return BookDto.convertToDto(savedBook);
	}	

	public Page<BookDto> getBooks(String title) {
	 	Pageable pageable = PageRequest.of(0, 20);
		return bookRepository.findByTitleContains(title, pageable).map(BookDto::convertToDto);
	}

	@Override
	public Page<BookDto> getBooks(Long authorId, Pageable pageable) {
		return bookRepository.findByAuthorId(authorId, pageable).map(BookDto::convertToDto);
	}

	public Page<BookDto> getBook(Long bookId, Long authorId, Pageable pageable) {
		return bookRepository.findByIdAndAuthorId(bookId, authorId, pageable).map(BookDto::convertToDto);
	}

}
