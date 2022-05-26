package com.brownbag.exercise4.controller;

import com.brownbag.exercise4.errorhandler.AuthorNotFoundException;
import com.brownbag.exercise4.errorhandler.BookNotFoundException;
import com.brownbag.exercise4.entity.Book;
import com.brownbag.exercise4.helpers.SortHelpers;
import com.brownbag.exercise4.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping("/authors/{authorId}/books/{id}")
    public ResponseEntity<Book> findById(
            @PathVariable Integer authorId,
            @PathVariable Integer id) throws BookNotFoundException {

        return new ResponseEntity<Book>(bookService.findById(id, authorId), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Integer id) throws BookNotFoundException {
        return new ResponseEntity<>(bookService.delete(id), HttpStatus.OK);
    }

    @PostMapping("/authors/{authorId}/books")
    public ResponseEntity save(@PathVariable Integer authorId, @RequestBody Book book)
    throws AuthorNotFoundException {
        return new ResponseEntity<>(bookService.save(authorId, book), HttpStatus.OK);
    }

    @PutMapping("/authors/{bookId}/{id}")
    private ResponseEntity<Book> put(@PathVariable Integer authorId,
                                     @PathVariable Integer id, @RequestBody Book book) throws
            BookNotFoundException, AuthorNotFoundException{
        return new ResponseEntity<>(bookService.update(authorId, id, book), HttpStatus.OK);
    }

    @GetMapping("/authors/{authorId}/books")
    public ResponseEntity find(@PathVariable Integer authorId,
                               @RequestParam(defaultValue = "0") Integer page,
                               @RequestParam(defaultValue = "10") Integer size,
                               @RequestParam(defaultValue = "title") String sortBy,
                               @RequestParam(defaultValue = "true") Boolean isAsc)
            throws AuthorNotFoundException {
        return new ResponseEntity<>(bookService.find(authorId, page, size,
                SortHelpers.getSort(sortBy, isAsc)), HttpStatus.OK);
    }
}
