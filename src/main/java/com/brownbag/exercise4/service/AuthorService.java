package com.brownbag.exercise4.service;

import com.brownbag.exercise4.entity.Author;
import com.brownbag.exercise4.errorhandler.AuthorNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

public interface AuthorService {

    public Author save(Author author);

    public Author update(Integer id, Author author)  throws AuthorNotFoundException;

    public Author delete(Integer id)  throws AuthorNotFoundException;

    public Author findById(Integer id)  throws AuthorNotFoundException;

    public Page<Author> find(Integer page, Integer size, Sort sort);
}
