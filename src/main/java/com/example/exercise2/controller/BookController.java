package com.example.exercise2.controller;

import com.example.exercise2.service.BookService;
import com.example.exercise2.service.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/books")
public class BookController {

  @Autowired
  private BookService bookService;

  @GetMapping
  public Page<Book> getBooksByTitleContaining(
      @RequestParam() String title,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "3") int size,
      @RequestParam(defaultValue = "id,desc") String[] sort) {

      return bookService.findBooksByTitleContaining(title, page, size, sort);
  }

}
