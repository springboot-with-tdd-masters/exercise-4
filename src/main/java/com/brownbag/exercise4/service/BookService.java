package com.brownbag.exercise4.service;

import com.brownbag.exercise4.errorhandler.AuthorNotFoundException;
import com.brownbag.exercise4.errorhandler.BookNotFoundException;
import com.brownbag.exercise4.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

public interface BookService {

    public Book save(Integer authorId, Book book) throws AuthorNotFoundException;

    public Book update(Integer authorId, Integer id, Book book)  throws BookNotFoundException
            , AuthorNotFoundException;

    public Book delete(Integer id)  throws BookNotFoundException;

    public Book findById(Integer authorId, Integer id)  throws BookNotFoundException;

    public Page<Book> find(Integer authorId, Integer page, Integer size, Sort sort);
}
