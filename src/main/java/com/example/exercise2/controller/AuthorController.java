package com.example.exercise2.controller;

import com.example.exercise2.service.AuthorService;
import com.example.exercise2.service.BookService;
import com.example.exercise2.service.model.Author;
import com.example.exercise2.service.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authors")
public class AuthorController {

  @Autowired
  AuthorService authorService;

  @Autowired
  BookService bookService;

  @PostMapping
  public Author createAuthor(@RequestBody Author author) {
    return authorService.save(author.getName());
  }

  @GetMapping
  public Page<Author> getAuthor(@RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "3") int size,
      @RequestParam(defaultValue = "id,desc") String[] sort) {
    return authorService.findAll(page, size, sort);
  }

  @GetMapping("/{id}")
  public Author getAuthorById(@PathVariable long id) {
    return authorService.findById(id);
  }

  @PostMapping("/{id}/books")
  public Book saveBook(@PathVariable long id, @RequestBody Book book) {
    Author author = authorService.findById(id);
    book.setAuthor(author);
    return bookService.save(book);
  }

  @GetMapping("/{authorId}/books/{bookId}")
  public Book getBookByAuthorIdAndBookId(@PathVariable long authorId, @PathVariable long bookId) {
    Author author = authorService.findById(authorId);
    return bookService.findById(bookId);
  }

  @GetMapping("/{authorId}/books")
  public Page<Book> getBooks(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "3") int size,
      @RequestParam(defaultValue = "id,desc") String[] sort) {

    return bookService.findAll(page, size, sort);
  }
}
