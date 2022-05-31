package com.example.demo.controller;

import com.example.demo.model.Author;
import com.example.demo.model.AuthorRequest;
import com.example.demo.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/authors")
public class AuthorController {

    @Autowired
    private AuthorService authorService;

    @PostMapping()
    public ResponseEntity<Void> createNewAuthor(@Valid @RequestBody AuthorRequest authorRequest, UriComponentsBuilder uriComponentsBuilder) {
        Long id = saveBook(authorRequest);
        UriComponents uriComponents = uriComponentsBuilder.path("api/authors/{id}").buildAndExpand(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(uriComponents.toUri());
        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }

    private Long saveBook(AuthorRequest authorRequest) {
        return authorService.createNewAuthor(authorRequest);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Author> readAuthorById(@PathVariable Long id) {
        return ResponseEntity.ok(authorService.readAuthorById(id));
    }

    @GetMapping()
    private ResponseEntity<Page<Author>> readAllAuthors(@PageableDefault(sort = "id", direction = Sort.Direction.ASC)
                                                        Pageable pageable) {
        return ResponseEntity.ok(authorService.readAllAuthors(pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Author> updateAuthor(@PathVariable Long id, @Valid @RequestBody AuthorRequest authorRequest) {
        return ResponseEntity.ok(authorService.updateAuthor(id, authorRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable("id") Long id) {
        authorService.deleteAuthorById(id);
        return ResponseEntity.ok().build();
    }
}
