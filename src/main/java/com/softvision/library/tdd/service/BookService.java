package com.softvision.library.tdd.service;

import com.softvision.library.tdd.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BookService {
    Book createOrUpdate(Book book);
    Page<Book> getAll(Pageable pageable);
    List<Book> getAll();
    Book getById(long id);
    Page<Book> getAllByAuthor(long id, Pageable pageable);
    void delete(long id);

    Page<Book> getContainingTitle(String nameInfix, Pageable pageable);
}
