package com.example.bookTDD.controller;

import com.example.bookTDD.exception.RecordNotFoundException;
import com.example.bookTDD.model.Author;
import com.example.bookTDD.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/authors")
@RestController
public class AuthorController {

    @Autowired
    private AuthorRepository authorRepository;

    @GetMapping
    public Page<Author> getAllAuthors(Pageable pageable) {
        return authorRepository.findAll(pageable);
    }

    @PostMapping
    public Author createAuthor(@RequestBody Author author) {
        return authorRepository.save(author);
    }

    @PutMapping("/{authorId}")
    Author updateAuthor(@PathVariable Long authorId, @RequestBody Author authorRequest) throws RecordNotFoundException {
        return authorRepository.findById(authorId).map(author -> {
            author.setName(authorRequest.getName());
            return authorRepository.save(author);
        }).orElseThrow(() -> new RecordNotFoundException("Author id: " + authorId + " not found!"));
    }


    @DeleteMapping("/{authorId}")
    public ResponseEntity<?> deleteAuthor(@PathVariable Long authorId) throws RecordNotFoundException {
        return authorRepository.findById(authorId).map(author -> {
            authorRepository.delete(author);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new RecordNotFoundException("Author id: " + authorId + " not found!"));
    }
}

