package com.example.basicauth.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.basicauth.model.dto.AuthorDto;

public interface AuthorService {
	AuthorDto createAuthor(String authorName);
	AuthorDto getAuthor(Long id);
	Page<AuthorDto> findAll(Pageable pageable);
}
