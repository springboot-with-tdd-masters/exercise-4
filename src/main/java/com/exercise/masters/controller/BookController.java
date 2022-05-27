/**
 * 
 */
package com.exercise.masters.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.exercise.masters.exceptions.RecordNotFoundException;
import com.exercise.masters.model.Book;
import com.exercise.masters.services.BookService;

/**
 * @author michaeldelacruz
 *
 */

@RestController
public class BookController {
	
	@Autowired
	private BookService bookService;
	
	@GetMapping("/author/{authorId}/books")
	public ResponseEntity<Page<Book>> findAllBooks(
			@PathVariable Long authorId,
            @RequestParam(defaultValue = "0") Integer pageNo, 
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy) throws RecordNotFoundException {
		
		Page<Book> books = bookService.findAllBooks(authorId, pageNo, pageSize, sortBy);
		return new ResponseEntity<Page<Book>>(books, new HttpHeaders(), HttpStatus.OK); 
	}

    @GetMapping("/author/{authorId}/books/{id}")
    public ResponseEntity<Book> findBookById(@PathVariable Long authorId, @PathVariable Long bookId) throws RecordNotFoundException {
    	Book book = bookService.findBookById(authorId, bookId);
        return new ResponseEntity<Book>(book, new HttpHeaders(), HttpStatus.OK);
    }
    
    @PostMapping("/author/{authorId}/books")
    public ResponseEntity<Book> saveBook(@PathVariable Long authorId, @RequestBody Book book) throws RecordNotFoundException {
    	Book savedBook = bookService.save(authorId, book);
        return new ResponseEntity<Book>(savedBook, new HttpHeaders(), HttpStatus.OK);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Book> deleteBookById(@PathVariable Long id) throws RecordNotFoundException {
    	Book book = bookService.deleteById(id);
        return new ResponseEntity<Book>(book, new HttpHeaders(), HttpStatus.OK);
    }
    
    @GetMapping("/books")
    public ResponseEntity<Book> findBookByName(@RequestParam String name) throws RecordNotFoundException {
    	Book book = bookService.findBookByName(name);
        return new ResponseEntity<Book>(book, new HttpHeaders(), HttpStatus.OK);
    }
	
}
