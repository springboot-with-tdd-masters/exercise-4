package com.softvision.books.controllers;

import com.softvision.books.services.BookService;
import com.softvision.books.services.domain.Book;
import com.softvision.books.services.domain.BookFilter;
import com.softvision.books.services.domain.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/api/rest/books")
public class BookController {

    private BookService bookService;

    @Autowired
    public void setBookService(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public Pagination<Book> search(@RequestParam String title,
                                   @RequestParam(defaultValue = "0") Integer page,
                                   @RequestParam(defaultValue = "10") Integer size,
                                   @RequestParam(required = false) String[] sort) {

        PageRequest pageRequest = PageRequest.of(page, size);

        if (Objects.nonNull(sort)) {
            pageRequest = PageRequest.of(page, size, buildSortFrom(sort));
        }

        final BookFilter bookFilter = BookFilter.of(title);

        return bookService.findAll(bookFilter, pageRequest);
    }

    private Sort buildSortFrom(String[] sortRequest) {

        if (sortRequest.length < 2) {
            return Sort.unsorted();
        }

        final String sortProperty = sortRequest[0];
        final String direction = sortRequest[1];

        return Sort.by(Sort.Direction.fromString(direction), sortProperty);
    }

}
