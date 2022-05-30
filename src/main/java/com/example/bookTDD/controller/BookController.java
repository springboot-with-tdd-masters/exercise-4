package com.example.bookTDD.controller;

import java.util.List;

import com.example.bookTDD.repository.AuthorRepository;
import com.example.bookTDD.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.example.bookTDD.exception.RecordNotFoundException;
import com.example.bookTDD.model.Book;
import com.example.bookTDD.service.BookService;

@RestController
public class BookController {

	@Autowired
	AuthorRepository authorRepository;

	@Autowired
	BookRepository bookRepository;

	@GetMapping("/authors/{authorId}/books")
	public Page<Book> getAllBooksByAuthorId(@PathVariable(value = "authorId") Long authorId, Pageable pageable) {
		return bookRepository.findByAuthorId(authorId, pageable);
	}

	@PostMapping("/authors/{authorId}/books")
	public Book createBook(@PathVariable(value = "authorId") Long authorId, @RequestBody Book book)
			throws RecordNotFoundException {
		return authorRepository.findById(authorId).map(author -> {
			book.setAuthor(author);
			return bookRepository.save(book);
		}).orElseThrow(() -> new RecordNotFoundException("Author id: " + authorId + " not found!"));
	}

	@PutMapping("/authors/{authorId}/books/{bookId}")
	public Book updateBook(@PathVariable(value = "authorId") Long authorId, @PathVariable(value = "bookId") Long bookId,
						   @RequestBody Book bookRequest) throws RecordNotFoundException {
		if (!authorRepository.existsById(authorId)) {
			throw new RecordNotFoundException("Author id: " + authorId + " not found!");
		}

		return bookRepository.findById(bookId).map(book -> {
			book.setTitle(bookRequest.getTitle());
			book.setDescription(bookRequest.getDescription());
			return bookRepository.save(book);
		}).orElseThrow(() -> new RecordNotFoundException("Book id: " + bookId + " not found!"));
	}

	@DeleteMapping("/authors/{authorId}/books/{bookId}")
	public ResponseEntity<?> deleteBook(@PathVariable Long authorId, @PathVariable Long bookId)
			throws RecordNotFoundException {
		return bookRepository.findByIdAndAuthorId(bookId, authorId).map(book -> {
			bookRepository.delete(book);
			return ResponseEntity.ok().build();
		}).orElseThrow(
				() -> new RecordNotFoundException("Book not found with id: " + bookId + " and author id: " + authorId));
	}

}