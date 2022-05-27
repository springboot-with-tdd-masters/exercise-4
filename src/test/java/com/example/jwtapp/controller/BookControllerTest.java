package com.example.jwtapp.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Date;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.example.jwtapp.model.dto.BookDto;
import com.example.jwtapp.service.AuthorService;
import com.example.jwtapp.service.BookService;

@WebMvcTest
@AutoConfigureMockMvc
public class BookControllerTest {
	
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private BookService bookService;
	
	@MockBean
	private AuthorService authorService;
	
	@Test
	@DisplayName("Get book by title")
	public void getBookByTitle() throws Exception {
	    BookDto book = new BookDto();
	    book.setId(1L);
	    book.setTitle("Harry Potter and the Sorcerer's Stone");
	    book.setDescription("1st book");
	    book.setCreatedDate(new Date());
	    book.setUpdatedDate(new Date());
	    book.setAuthorId(1L);
	  
	    BookDto book2 = new BookDto();
	    book2.setId(2L);
	    book2.setTitle("Harry Potter and the Chamber of Secrets");
	    book2.setDescription("2nd book");
	    book2.setCreatedDate(new Date());
	    book2.setUpdatedDate(new Date());
	    book2.setAuthorId(1L);
	  
	    
		Page<BookDto> pagedBooks = new PageImpl(Arrays.asList(book, book2));

		Pageable pageable = PageRequest.of(0, 1);

		when(bookService.getBooks("Harry Potter"))
			.thenReturn(pagedBooks);
	
		this.mockMvc.perform(get("/books?title=Harry Potter"))
			.andExpect(status().isOk())
			.andExpect(MockMvcResultMatchers.jsonPath("$.content.[0].id").value("1"))
			.andExpect(MockMvcResultMatchers.jsonPath("$.content.[0].title").value("Harry Potter and the Sorcerer's Stone"))
			.andExpect(MockMvcResultMatchers.jsonPath("$.content.[0].description").value("1st book"))
			.andExpect(MockMvcResultMatchers.jsonPath("$.content.[1].id").value("2"))
			.andExpect(MockMvcResultMatchers.jsonPath("$.content.[1].title").value("Harry Potter and the Chamber of Secrets"))
			.andExpect(MockMvcResultMatchers.jsonPath("$.content.[1].description").value("2nd book"));
		
		verify(bookService).getBooks("Harry Potter");
	}	

}
