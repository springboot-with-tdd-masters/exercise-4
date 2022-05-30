package com.softvision.books.services;

import com.softvision.books.services.domain.Author;
import com.softvision.books.services.domain.Pagination;
import org.springframework.data.domain.Pageable;

public interface AuthorService {

    Pagination<Author> findAll(Pageable pageable);

    Author findById(Long id);

    Author save(Author author);

    Author update(Long id, Author author);

    void deleteById(Long id);
}
