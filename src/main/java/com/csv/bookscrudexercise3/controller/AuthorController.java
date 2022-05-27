package com.csv.bookscrudexercise3.controller;

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

import com.csv.bookscrudexercise3.exception.RecordNotFoundException;
import com.csv.bookscrudexercise3.model.Author;
import com.csv.bookscrudexercise3.model.Book;
import com.csv.bookscrudexercise3.model.BookUpdate;
import com.csv.bookscrudexercise3.service.AuthorService;
import com.csv.bookscrudexercise3.service.BookService;

@RestController
@RequestMapping(value="/authors")
public class AuthorController {

	@Autowired
	AuthorService authorService;
	
	@Autowired
	BookService bookService;
	
	@GetMapping()
	public Page<Author> getAllAuthors(@PageableDefault Pageable pageable) {
		return authorService.getAllAuthors(pageable);
	}
	
	@GetMapping("/{author_id}")
	public Author getAuthorById(@PathVariable("author_id") Long authorId) throws RecordNotFoundException {
		return authorService.getAuthorById(authorId);
	}
	
	@PostMapping
	public Author createAuthor(@RequestBody Author author) throws RecordNotFoundException {
		return authorService.createAuthor(author);
	}
	
	@GetMapping("/{author_id}/books")
	public Page<Book> getAllBooksByAuthorId(@PathVariable("author_id") Long authorId, @PageableDefault Pageable pageable) throws RecordNotFoundException {
		return bookService.getBooksByAuthorId(authorId, pageable);
	}
	
	@GetMapping("/{author_id}/books/{book_id}")
	public Book getAllBooksByAuthorIdAndByBookId(@PathVariable("author_id") Long authorId, @PathVariable("book_id") Long bookId) throws RecordNotFoundException {
		return bookService.getBookByAuthorIdAndByBookId(authorId, bookId);
	}
	
	@PostMapping("/{author_id}/books")
	public Book createOrUpdateBook(@PathVariable("author_id") Long authorId, @RequestBody Book book) throws RecordNotFoundException {
		return bookService.createBook(authorId, book);
	}
	
	@PutMapping("/{author_id}/books/{book_id}")
	public Book updateBook(@PathVariable("author_id") Long authorId, @PathVariable("book_id") Long bookId, @RequestBody BookUpdate newBook) throws RecordNotFoundException {
		Book book = bookService.getBookByAuthorIdAndByBookId(authorId, bookId);
		return bookService.updateBook(newBook, book);
	}
	
	@DeleteMapping("/{author_id}/books/{book_id}")
	public void deleteBook(@PathVariable("author_id") Long authorId, @PathVariable("book_id") Long bookId) throws RecordNotFoundException {
		bookService.deleteBookById(authorId, bookId);
	}
	
}
