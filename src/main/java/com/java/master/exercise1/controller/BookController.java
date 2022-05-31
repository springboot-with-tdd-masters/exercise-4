package com.java.master.exercise1.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.java.master.exercise1.model.Book;
import com.java.master.exercise1.service.BookService;

@RestController
@RequestMapping("/book")
public class BookController {

	@Autowired
	private BookService bookService;

	@PostMapping
	public Book createUpdateBook(@RequestBody Book book) {
		return bookService.createUpdateBook(book);
	}

	@GetMapping
	public List<Book> getAllBooks() {
		return bookService.getAllBooks();
	}

	@DeleteMapping("/delete/{id}")
	public Book deleteBookById(@PathVariable Long id) {
		return bookService.deleteBookById(id);
	}

	@GetMapping("/{id}")
	public Book getBookById(@PathVariable Long id) {
		return bookService.getBookById(id);
	}
}
