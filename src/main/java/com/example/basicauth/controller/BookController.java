package com.example.basicauth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.basicauth.model.dto.BookDto;
import com.example.basicauth.service.BookService;

@RestController
@RequestMapping("/books") 
public class BookController {
	
	@Autowired
	private BookService bookService;
	
	@GetMapping
	public Page<BookDto> getBooksByTitle(@RequestParam String title) {
		return bookService.getBooks(title);
	}
	
}
