package com.example.jwtapp.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.jwtapp.model.dto.AuthorDto;
import com.example.jwtapp.model.dto.BookDto;
import com.example.jwtapp.model.dto.BookRequest;
import com.example.jwtapp.service.AuthorService;
import com.example.jwtapp.service.BookService;

@RestController
@RequestMapping("/authors") 
public class AuthorController {
	
	@Autowired
	private AuthorService authorService;
	
	@Autowired
	private BookService bookService;
	
	@PostMapping
	public AuthorDto createAuthor(@RequestBody Map<String, String> request) {
		return authorService.createAuthor(request.get("name"));
	}
	
	@GetMapping("/{id}")
	public AuthorDto getAuthor(@PathVariable Long id) {
		return authorService.getAuthor(id);
	}
	
	@GetMapping
	public Page<AuthorDto> getAuthors(@RequestParam Map<String, String> params) {
		int page = Integer.valueOf(params.get("page"));
		int size = Integer.valueOf(params.get("size"));
		String[] sort = params.get("sort").split(",");
		
		Sort.Direction direction = Sort.Direction.fromString(sort[1]);
		
		Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort[0]));
		return authorService.findAll(pageable);
	}
	
	@PostMapping("{authorId}/books")
	public BookDto addBook(@PathVariable Long authorId, @RequestBody BookRequest request) {
		return bookService.addBook(authorId, request);
	}
	
	@GetMapping("{authorId}/books")
	public Page<BookDto> getBooksByAuthor(@PathVariable Long authorId, @RequestParam Map<String, String> params) {
		int page = Integer.valueOf(params.get("page"));
		int size = Integer.valueOf(params.get("size"));
		String[] sort = params.get("sort").split(",");
		
		Sort.Direction direction = Sort.Direction.fromString(sort[1]);
		
		Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort[0]));
		
		return bookService.getBooks(authorId, pageable);
	}
	
	@GetMapping("{authorId}/books/{bookId}")
	public Page<BookDto> getBookById(@PathVariable Long authorId, @PathVariable Long bookId) {
		Pageable pageable = PageRequest.of(0, 1);
		return bookService.getBook(bookId, authorId, pageable);
	}
}
