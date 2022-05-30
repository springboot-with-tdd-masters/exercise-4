package com.csv.bookscrudexercise4.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.csv.bookscrudexercise4.exception.RecordNotFoundException;
import com.csv.bookscrudexercise4.model.Author;

@Service
public interface AuthorService {

	public Page<Author> getAllAuthors(Pageable pageable);
	
	public Author getAuthorById(Long id) throws RecordNotFoundException;
	
	public Author createAuthor(Author authorEntity);
	
}
