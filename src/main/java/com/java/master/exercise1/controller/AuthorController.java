package com.java.master.exercise1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.java.master.exercise1.model.Author;
import com.java.master.exercise1.model.Book;
import com.java.master.exercise1.model.BookRequest;
import com.java.master.exercise1.service.AuthorService;
import com.java.master.exercise1.service.BookService;

@RestController
@RequestMapping("/authors")
public class AuthorController {

	@Autowired
	private AuthorService service;

	@Autowired
	private BookService bookService;

	@PostMapping
	public Author create(@RequestBody Author author) {
		return service.create(author);
	}

	@GetMapping("/getById/{id}")
	public Author getById(@PathVariable Long id) {
		return service.getAuthorById(id);
	}

	@GetMapping
	public Page<Author> getAll(Pageable pageable) {
		return service.getAuthors(pageable);
	}

	@PostMapping("/{id}/books")
	public Book createUpdateBook(Long id, Book book) {
		return bookService.createBook(id, book);
	}

	@GetMapping("/{authorId}")
	public Page<Book> getAllBooksByAuthorId(@PathVariable("authorId") Long authorId,
			@PageableDefault Pageable pageable) {
		return bookService.getBooksByAuthorId(authorId, pageable);
	}

	@GetMapping("/{authorId}/books/{bookId}")
	public Book getAllBooksByAuthorIdAndBookId(@PathVariable("authorId") Long authorId,
			@PathVariable("bookId") Long bookId) {
		return bookService.getBookByAuthorIdAndByBookId(authorId, authorId);

	}

	@PutMapping("/{authorId}/books/{bookId}")
	public Book updateBook(@PathVariable("authorId") Long authorId, @PathVariable("bookId") Long bookId,
			@RequestBody BookRequest newBook) {
		return bookService.updateBook(authorId, bookId, newBook);
	}

	@DeleteMapping("/{authorId}/books/{bookId}")
	public void deleteBook(@PathVariable("authorId") Long authorId, @PathVariable("bookId") Long bookId) {
		bookService.deleteBookById(authorId, bookId);
	}
}
