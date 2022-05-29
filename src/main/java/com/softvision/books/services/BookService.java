package com.softvision.books.services;

import com.softvision.books.services.domain.Book;
import com.softvision.books.services.domain.BookFilter;
import com.softvision.books.services.domain.Pagination;
import org.springframework.data.domain.Pageable;

public interface BookService {

    Pagination<Book> findAll(BookFilter filter, Pageable pageable);

    Book findById(Long id);

    Book add(Long authorId, Book book);

    Book update(Long id, Book book);

    void deleteById(Long id);
}
