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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.exercise.masters.exceptions.RecordNotFoundException;
import com.exercise.masters.model.Author;
import com.exercise.masters.services.AuthorService;

/**
 * @author michaeldelacruz
 *
 */

@RestController
@RequestMapping("/author")
public class AuthorController {

	@Autowired
	private AuthorService authorService;
	
	@GetMapping
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<Page<Author>> findAllBooks(
            @RequestParam(defaultValue = "0") Integer pageNo, 
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy) throws RecordNotFoundException {
		
		Page<Author> authors = authorService.findAllAuthors(pageNo, pageSize, sortBy);
		return new ResponseEntity<Page<Author>>(authors, new HttpHeaders(), HttpStatus.OK); 
	}

    @GetMapping("/{id}")
    public ResponseEntity<Author> findAuthorById(@PathVariable Long id) throws RecordNotFoundException {
    	Author author = authorService.findAuthorById(id);
        return new ResponseEntity<Author>(author, new HttpHeaders(), HttpStatus.OK);
    }
    
    @PostMapping
    public ResponseEntity<Author> saveAuthor(@RequestBody Author author) throws RecordNotFoundException {
    	Author savedAuthor = authorService.save(author);
        return new ResponseEntity<Author>(savedAuthor, new HttpHeaders(), HttpStatus.OK);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Author> deleteAuthorById(@PathVariable Long id) throws RecordNotFoundException {
    	Author author = authorService.deleteById(id);
        return new ResponseEntity<Author>(author, new HttpHeaders(), HttpStatus.OK);
    }
}
