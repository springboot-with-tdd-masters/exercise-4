package com.java.master.exercise4.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.java.master.exercise4.model.Author;

public interface AuthorService {

	Author create(Author author);
	
	Page<Author> getAuthors(Pageable pageable);
	
	Author getAuthorById(Long id);
}
