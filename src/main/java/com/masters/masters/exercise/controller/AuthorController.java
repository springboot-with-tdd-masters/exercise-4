package com.masters.masters.exercise.controller;

import com.masters.masters.exercise.exception.RecordNotFoundException;
import com.masters.masters.exercise.model.*;
import com.masters.masters.exercise.services.AuthorService;
import com.masters.masters.exercise.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/authors")
public class AuthorController {

    @Autowired
    private AuthorService authorService;

    @Autowired
    private BookService bookService;

    @PostMapping
    public ResponseEntity<Author> saveAuthor(@RequestBody AuthorDtoRequest request){
        Author response = authorService.saveAuthor(request);
        return new ResponseEntity<Author>(response, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<Author> findAuthorById(@PathVariable Long id) throws RecordNotFoundException {
        Author response = authorService.findById(id);
        return new ResponseEntity<Author>(response, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<Author>> findAuthorByAllAuthors(Pageable pageRequest) throws RecordNotFoundException {
        Page<Author>list = authorService.findAllAuthors(pageRequest);
        return new ResponseEntity<Page<Author>>(list, new HttpHeaders(), HttpStatus.OK);
    }

    @PostMapping("{id}/books")
    public ResponseEntity<BookEntity> saveOrUpdateBook(@RequestBody BookDtoRequest book,@PathVariable Long id) throws RecordNotFoundException {
        BookEntity response = bookService.save(book,id);
        return new ResponseEntity<BookEntity>(response, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("{id}/books/{bookId}")
    public ResponseEntity<Page<BookEntity>> getBookByAuthorIdAndId(@PathVariable Long id,@PathVariable Long bookId, Pageable pageRequest) throws RecordNotFoundException {
        Page<BookEntity> list = bookService.findByAuthorIdAndBookId(id,bookId,pageRequest);
        return new ResponseEntity<Page<BookEntity>>(list, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("{id}/books")
    public ResponseEntity<Page<BookEntity>> getBooksByAuthorId(@PathVariable Long id, Pageable pageRequest) throws RecordNotFoundException {
        Page<BookEntity> list = bookService.findByAuthorId(id,pageRequest);
        return new ResponseEntity<Page<BookEntity>>(list, new HttpHeaders(), HttpStatus.OK);
    }

}
