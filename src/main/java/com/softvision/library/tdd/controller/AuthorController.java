package com.softvision.library.tdd.controller;

import com.softvision.library.tdd.model.Author;
import com.softvision.library.tdd.model.Book;
import com.softvision.library.tdd.service.AuthorService;
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

@RestController
@RequestMapping("/authors")
public class AuthorController {

    @Autowired
    AuthorService authorService;
    @Autowired
    BookService bookService;

    @PostMapping
    public ResponseEntity<Author> create(@RequestBody Author author) {
        return new ResponseEntity<>(authorService.create(author), new HttpHeaders(), HttpStatus.CREATED);
    }

    public ResponseEntity<Page<Author>> getAll(@PageableDefault Pageable page) {
        return new ResponseEntity<>(authorService.getAll(page), new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<Author>> getContainingName(@Param("name") String name, @PageableDefault Pageable page) {
        if (name == null) {
            return getAll(page);
        }
        return new ResponseEntity<>(authorService.getContainingName(name, page), new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Author> getById(@PathVariable long id) {
        return new ResponseEntity<>(authorService.getById(id), new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/{id}/books")
    public ResponseEntity<Page<Book>> getAllBooksById(@PathVariable long id, @PageableDefault Pageable page) {
        return new ResponseEntity<>(bookService.getAllByAuthor(id, page), new HttpHeaders(), HttpStatus.OK);
    }

    @PostMapping("/{id}/books")
    public ResponseEntity<Book> create(@PathVariable long id, @RequestBody Book book) {
        return new ResponseEntity<>(authorService.createBook(id, book), new HttpHeaders(), HttpStatus.CREATED);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Long> delete(@PathVariable long id) {
        authorService.delete(id);
        return new ResponseEntity<>(id, new HttpHeaders(), HttpStatus.OK);
    }
}
