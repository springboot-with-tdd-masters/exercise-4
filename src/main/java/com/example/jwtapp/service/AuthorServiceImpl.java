package com.example.jwtapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.jwtapp.exception.AuthorNotFoundException;
import com.example.jwtapp.model.Author;
import com.example.jwtapp.model.dto.AuthorDto;
import com.example.jwtapp.repository.AuthorRepository;
import com.example.jwtapp.repository.BookRepository;

@Service
public class AuthorServiceImpl implements AuthorService {
	
	@Autowired
	private AuthorRepository authorRepository;
	
	@Autowired
	private BookRepository bookRepository;
	
	public AuthorDto createAuthor(String authorName) {
		Author author = new Author();
		author.setName(authorName);
		
		Author savedAuthor = authorRepository.save(author);
		
		return AuthorDto.convertToDto(savedAuthor);
	}

	public AuthorDto getAuthor(Long id) {
		return authorRepository.findById(id)
				.map(AuthorDto::convertToDto)
				.orElseThrow(AuthorNotFoundException::new);
	}

	public Page<AuthorDto> findAll(Pageable pageable) {
		return authorRepository.findAll(pageable).map(AuthorDto::convertToDto);
	}

}
