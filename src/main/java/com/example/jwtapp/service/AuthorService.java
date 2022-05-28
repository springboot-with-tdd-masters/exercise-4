package com.example.jwtapp.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.jwtapp.model.dto.AuthorDto;

public interface AuthorService {
	AuthorDto createAuthor(String authorName);
	AuthorDto getAuthor(Long id);
	Page<AuthorDto> findAll(Pageable pageable);
}
