package com.csv.bookscrudexercise3.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.csv.bookscrudexercise3.exception.RecordNotFoundException;
import com.csv.bookscrudexercise3.model.Author;

@Service
public interface AuthorService {

	public Page<Author> getAllAuthors(Pageable pageable);
	
	public Author getAuthorById(Long id) throws RecordNotFoundException;
	
	public Author createAuthor(Author authorEntity);
	
}
