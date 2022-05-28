package com.example.basicauth.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.example.basicauth.repository.AuthorRepository;
import com.example.basicauth.exception.AuthorNotFoundException;
import com.example.basicauth.model.Author;
import com.example.basicauth.model.dto.AuthorDto;

public class AuthorServiceTest {

	@Mock
	private AuthorRepository authorRepository;
	
	@InjectMocks
	private AuthorServiceImpl authorService;
	
	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}
	
	@Test
	@DisplayName("Should save Author and return details")
	public void shouldSaveAuthor() {
		String authorName = "JK Rowling";
		
		Author newAuthor = new Author();
		newAuthor.setName(authorName);
		
		//expected response
		Author expectedResponse = new Author();
		expectedResponse.setId(1L);
		expectedResponse.setName(authorName);
		
		Date today = new Date();
		expectedResponse.setCreatedDate(today);		
		expectedResponse.setUpdatedDate(today);

		when(authorRepository.save(newAuthor))
			.thenReturn(expectedResponse);
		
		AuthorDto actualResponse = authorService.createAuthor(authorName);
		
		assertAll(
			    () -> assertEquals(expectedResponse.getCreatedDate(), actualResponse.getCreatedDate()),
	            () -> assertEquals(expectedResponse.getCreatedDate(), actualResponse.getCreatedDate()),	            
				() -> assertEquals(expectedResponse.getId(), actualResponse.getId()),
	            () -> assertEquals(expectedResponse.getName(), actualResponse.getName())
	        );		
	}
	
	@Test
	@DisplayName("Should return author with correct details")
	public void shouldReturnAuthorWithCorrectDetails() {
		Author expectedResponse = new Author();
		expectedResponse.setId(1L);
		expectedResponse.setName("JK Rowling");
		
		Date today = new Date();
		expectedResponse.setCreatedDate(today);		
		expectedResponse.setUpdatedDate(today);

		when(authorRepository.findById(1L))
			.thenReturn(Optional.of(expectedResponse));
		
		AuthorDto actualResponse = authorService.getAuthor(1L);
		
		assertAll(
			    () -> assertEquals(expectedResponse.getCreatedDate(), actualResponse.getCreatedDate()),
	            () -> assertEquals(expectedResponse.getCreatedDate(), actualResponse.getCreatedDate()),	            
				() -> assertEquals(expectedResponse.getId(), actualResponse.getId()),
	            () -> assertEquals(expectedResponse.getName(), actualResponse.getName())
	        );		
	}
	
	@Test
	@DisplayName("Should throw AuthorNotFoundException when record does not exist")
	public void shouldThrowExceptionWhenAuthorNotExist() {
		when(authorRepository.findById(1L))
			.thenReturn(Optional.empty());
		
		assertThrows(AuthorNotFoundException.class, () -> authorService.getAuthor(1L));
	}
	
	@Test
	@DisplayName("Should return all authors with pagination")
	public void shouldReturnAllAuthorsWithPagination() {
		int page = 0;
		int size = 3;
		String sort = "name";
		
		Pageable pageable = PageRequest.of(page, size, Sort.by(sort));

		Author author1 = new Author();
		author1.setId(1L);
		author1.setName("J. K. Rowling");
		
		Author author2 = new Author();
		author2.setId(2L);
		author2.setName("J. R. R. Tolkien");
				
		Page<Author> pagedAuthors = new PageImpl(Arrays.asList(author1, author2));
		
		when(authorRepository.findAll(pageable))
			.thenReturn(pagedAuthors);
		
		Page<AuthorDto> pagedAuthorList = authorService.findAll(pageable);
		
		assertAll(
			    () -> assertEquals(2, pagedAuthorList.getNumberOfElements()),
			    () -> assertEquals(1L, pagedAuthorList.getContent().get(0).getId()),
			    () -> assertEquals("J. K. Rowling", pagedAuthorList.getContent().get(0).getName()),
			    () -> assertEquals(2L, pagedAuthorList.getContent().get(1).getId()),
			    () -> assertEquals("J. R. R. Tolkien", pagedAuthorList.getContent().get(1).getName())			    
	        );		
	}
}
