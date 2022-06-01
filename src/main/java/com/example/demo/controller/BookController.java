package com.example.demo.controller;

import com.example.demo.model.Book;
import com.example.demo.model.BookRequest;
import com.example.demo.service.BookService;
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
import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @PostMapping()
    public ResponseEntity<Void> createNewBook(@Valid @RequestBody BookRequest bookrequest, UriComponentsBuilder uriComponentsBuilder) {
        Long id = saveBook(bookrequest);
        UriComponents uriComponents = uriComponentsBuilder.path("api/books/{id}").buildAndExpand(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(uriComponents.toUri());
        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }

    private Long saveBook(BookRequest bookrequest) {
        return bookService.createNewBook(bookrequest);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> readBookById(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.readBookById(id));
    }

    @GetMapping("/readByTitle")
    private ResponseEntity<Page<Book>> readBookByTitle(@RequestParam String title,
                                                          @PageableDefault(sort = "title", direction = Sort.Direction.ASC)
                                                          Pageable pageable) {
        return ResponseEntity.ok(bookService.readBookByTitle(title, pageable));
    }

    @GetMapping()
    private ResponseEntity<Page<Book>> readAllBooks(@PageableDefault(sort = "id", direction = Sort.Direction.ASC)
                                                        Pageable pageable) {
        return ResponseEntity.ok(bookService.readAllBooks(pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @Valid @RequestBody BookRequest bookRequest) {
        return ResponseEntity.ok(bookService.updateBook(id, bookRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable("id") Long id) {
        bookService.deleteBookById(id);
        return ResponseEntity.ok().build();
    }
}
