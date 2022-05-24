package com.softvision.library.tdd.controller;

import com.softvision.library.tdd.model.Book;
import com.softvision.library.tdd.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    BookService bookService;

    public ResponseEntity<List<Book>> getAll() {
        return new ResponseEntity<>(bookService.getAll(), new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<Book> get(@PathVariable long id) {
        return new ResponseEntity<>(bookService.getById(id), new HttpHeaders(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Book> create(@RequestBody Book book) {
        return new ResponseEntity<>(bookService.createOrUpdate(book), new HttpHeaders(), HttpStatus.CREATED);
    }

    public ResponseEntity<Page<Book>> getAll(@PageableDefault Pageable page) {
        return new ResponseEntity<>(bookService.getAll(page), new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<Book>> getAll(@Param("title") String title, @PageableDefault Pageable page) {
        if (title == null) {
            return getAll(page);
        }
        return new ResponseEntity<>(bookService.getContainingTitle(title, page), new HttpHeaders(), HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Long> delete(@PathVariable long id) {
        bookService.delete(id);
        return new ResponseEntity<>(id, new HttpHeaders(), HttpStatus.OK);
    }
}
