package com.softvision.books.controllers;

import com.softvision.books.services.AuthorService;
import com.softvision.books.services.domain.Author;
import com.softvision.books.services.domain.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rest/authors")
public class AuthorController {

    private AuthorService authorService;

    @Autowired
    public void setAuthorService(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping
    public Pagination<Author> getAll(Pageable pageable) {
        return authorService.findAll(pageable);
    }

    @GetMapping("{id:\\d+}")
    public ResponseEntity<Author> get(@PathVariable Long id) {

        final Author author = authorService.findById(id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(author);
    }

    @PostMapping
    public ResponseEntity<Author> create(@RequestBody Author author) {

        final Author savedAuthor = authorService.save(author);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedAuthor);
    }

    @PutMapping("{id:\\d+}")
    public ResponseEntity<Author> update(@PathVariable Long id, @RequestBody Author author) {

        final Author updatedAuthor = authorService.update(id, author);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(updatedAuthor);
    }

    @DeleteMapping("{id:\\d+}")
    public ResponseEntity<String> delete(@PathVariable Long id) {

        authorService.deleteById(id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Author successfully deleted");
    }
}
