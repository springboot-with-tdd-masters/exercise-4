package com.csv.bookscrudexercise3.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.csv.bookscrudexercise3.model.Book;
import com.csv.bookscrudexercise3.service.BookService;

@RestController
@RequestMapping(value="/books")
public class BookController {

	@Autowired
	BookService bookService;
	
	@GetMapping()
	public Page<Book> getAllBooksContaining(@RequestParam String title, Pageable pageable){
		return bookService.getBooksByTitleContaining(title, pageable);
	}
}
