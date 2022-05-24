package com.softvision.library.tdd.service;

import com.softvision.library.tdd.model.Author;
import com.softvision.library.tdd.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AuthorService {
    public Author create(Author author);

    Page<Author> getAll(Pageable page);

    Author getById(long id);

    Book createBook(long id, Book book);

    void delete(long id);

    Page<Author> getContainingName(String containsInfix, Pageable pageable);
}
