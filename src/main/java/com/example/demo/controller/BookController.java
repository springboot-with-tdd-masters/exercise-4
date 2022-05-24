package com.example.demo.controller;

import com.example.demo.model.Book;
import com.example.demo.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1")
public class BookController {

    final static Logger logger = Logger.getLogger("BookController");

    @Autowired
    BookService bookService;

    @PostMapping("/books")
    public ResponseEntity<Book> addBook(@RequestBody Book book){
        logger.info("Adding Book...");
        Long id = bookService.addBook(book).getId();
        Optional<Book> newBook = bookService.getBook(id);

        return new ResponseEntity<Book>(newBook.get(),HttpStatus.CREATED);
    }

    @PutMapping("/books")
    public ResponseEntity<Book> updateBook(@RequestBody Book book){
        logger.info("Updating Book...");
        bookService.addBook(book);
        return new ResponseEntity<Book>(book,HttpStatus.OK);
    }

    @GetMapping("/books/{id}")
    public Optional<Book> getBook(@PathVariable Long id){
        logger.info("Getting Book...");
        return bookService.getBook(id);
    }

    @GetMapping("/books")
    public List<Book> getBooks(){
        logger.info("Getting All Books...");
        if(bookService.getBooks().isEmpty()){
            //TODO:if empty return no record found
            System.out.println("empty");
        }
        return bookService.getBooks();
    }

    @DeleteMapping("/books/{id}")
    public String deleteBook(@PathVariable Long id){
        logger.info("Deleting Book...");
        bookService.deleteBook(id);
        return "Book Deleted with reference Id: "+id;
    }
}
