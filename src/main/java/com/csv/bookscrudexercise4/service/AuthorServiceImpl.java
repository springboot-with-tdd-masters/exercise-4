package com.csv.bookscrudexercise4.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.csv.bookscrudexercise4.exception.RecordNotFoundException;
import com.csv.bookscrudexercise4.model.Author;
import com.csv.bookscrudexercise4.repository.AuthorRepository;

@Service
public class AuthorServiceImpl implements AuthorService {

	@Autowired
	AuthorRepository repository;

	@Override
	public Page<Author> getAllAuthors(Pageable pageable) {
		return repository.findAll(pageable);
	}

	@Override
	public Author getAuthorById(Long id) throws RecordNotFoundException {
		return repository.findById(id).orElseThrow(RecordNotFoundException::new);	
	}

	@Override
	public Author createAuthor(Author authorEntity) {
		authorEntity = repository.save(authorEntity);
		return authorEntity;
	}

}
