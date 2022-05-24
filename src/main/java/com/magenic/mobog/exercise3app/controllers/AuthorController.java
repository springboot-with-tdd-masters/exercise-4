package com.magenic.mobog.exercise3app.controllers;

import com.magenic.mobog.exercise3app.requests.AddBookRequest;
import com.magenic.mobog.exercise3app.requests.AddAuthorRequest;
import com.magenic.mobog.exercise3app.responses.AuthorResponse;
import com.magenic.mobog.exercise3app.responses.BookResponse;
import com.magenic.mobog.exercise3app.responses.PageResponse;
import com.magenic.mobog.exercise3app.services.AuthorService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/authors")
public class AuthorController {

	private final AuthorService service;
	
	public AuthorController(AuthorService authorService) {
		this.service = authorService;
	}

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, path = "/{authorId}/books")
    BookResponse addBook(@PathVariable Long authorId, @RequestBody AddBookRequest newBook) {
		return service.addBookToAuthor(authorId, newBook);
	}
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	AuthorResponse addAuthor(@RequestBody AddAuthorRequest request){
		return service.createAuthor(request);
	}
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, path ="/{authorId}/books")
	PageResponse<BookResponse> getAllBooks(@PathVariable("authorId") Long authorId, @RequestParam(required = false) String title, Pageable p){
		if(Optional.ofNullable(title).isPresent()){
			return service.findBooksContainingTitle(title, p);
		}
		return service.findBookByAuthorWithPage(authorId, p);
	}
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	PageResponse<AuthorResponse> getAllAuthors(Pageable p){
		return service.findAuthorsWithPage(p);
	}

	@GetMapping(path= "/{authorId}")
	AuthorResponse getAuthorById(@PathVariable("authorId") Long authorId){
		return service.getAuthorById(authorId);
	}

	@GetMapping(path = "/{authorId}/books/{bookId}")
	BookResponse getBookById(@PathVariable("authorId") Long authorId, @PathVariable("bookId") Long bookId){
		return service.getBookMadeByAuthor(bookId, authorId);
	}
}
