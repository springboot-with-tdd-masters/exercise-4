package com.example.exercisefour.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.exercisefour.model.Author;
import com.example.exercisefour.model.Book;
import com.example.exercisefour.response.AuthorResponse;
import com.example.exercisefour.response.BookResponse;
import com.example.exercisefour.service.BookService;

@Controller
@RequestMapping(value = "/authors")
public class BookController {
	
	@Autowired
	BookService service;
	
	@PostMapping
	public ResponseEntity<AuthorResponse> createAuthor(@RequestBody Author author) {
		AuthorResponse newAuthor = service.createAuthor(author);
		
		return new ResponseEntity<AuthorResponse>(newAuthor, new HttpHeaders(), HttpStatus.OK);
	}
	
	@GetMapping
	public ResponseEntity<Page<AuthorResponse>> getAllAuthors(Pageable page) {
		Page<AuthorResponse> authors = service.getAllAuthors(page);
		
		return new ResponseEntity<Page<AuthorResponse>>(authors, new HttpHeaders(), HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<AuthorResponse> getAuthorById(@PathVariable("id") Long id) {
		AuthorResponse author = service.getAuthorById(id);
		
		return new ResponseEntity<AuthorResponse>(author, new HttpHeaders(), HttpStatus.OK);
	}
	
	@PostMapping("/{id}/books")
	public ResponseEntity<BookResponse> createBook(@PathVariable("id") Long id, @RequestBody Book book) {
		BookResponse newBook = service.createBook(id, book);
		
		return new ResponseEntity<BookResponse>(newBook, new HttpHeaders(), HttpStatus.OK);
	}
	
	@GetMapping("/{id}/books")
	public ResponseEntity<Page<BookResponse>> getAllBooks(@PathVariable("id") Long id, Pageable page) {
		Page<BookResponse> books = service.getAllBooks(id, page);
		
		return new ResponseEntity<Page<BookResponse>>(books, new HttpHeaders(), HttpStatus.OK);
	}
	
	@GetMapping("/{authorId}/books/{bookId}")
	public ResponseEntity<Page<BookResponse>> getBookById(@PathVariable("authorId") Long authorId, @PathVariable("bookId") Long bookId, Pageable page) {
		Page<BookResponse> book = service.getBookById(authorId, bookId, page);
		
		return new ResponseEntity<Page<BookResponse>>(book, new HttpHeaders(), HttpStatus.OK);
	}
	
	@GetMapping("/books")
	public ResponseEntity<Page<BookResponse>> getBooksByTitle(@RequestParam String title, Pageable page) {
		Page<BookResponse> books = service.getBookByTitle(title, page);
		
		return new ResponseEntity<Page<BookResponse>>(books, new HttpHeaders(), HttpStatus.OK);
	}
	
	@PutMapping("/{authorId}/books/{bookId}")
	public ResponseEntity<BookResponse> updateBook(@PathVariable("authorId") Long authorId, @PathVariable("bookId") Long bookId, @RequestBody Book book) {
		BookResponse newBook = service.updateBook(authorId, bookId, book);
		
		return new ResponseEntity<BookResponse>(newBook, new HttpHeaders(), HttpStatus.OK);
	}
	
	@DeleteMapping("/{authorId}/books/{bookId}")
	public ResponseEntity<Void> deleteABook(@PathVariable("authorId") Long authorId, @PathVariable("bookId") Long bookId) {
		service.deleteByBookId(authorId, bookId);
		
		return ResponseEntity.noContent().build();
	}
}
