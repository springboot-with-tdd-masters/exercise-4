package com.java.master.exercise1.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.java.master.exercise1.model.Author;
import com.java.master.exercise1.repository.AuthorRepository;
import com.java.master.exercise1.service.AuthorService;

@Service
public class AuthorServiceImpl implements AuthorService {
	
	@Autowired
	private AuthorRepository authorRepo;

	@Override
	public Author create(Author author) {
		return authorRepo.save(author);
	}

	@Override
	public Page<Author> getAuthors(Pageable pageable) {
		return authorRepo.findAll(pageable);
	}

	@Override
	public Author getAuthorById(Long id) {
		return authorRepo.findById(id).get();
	}

}
