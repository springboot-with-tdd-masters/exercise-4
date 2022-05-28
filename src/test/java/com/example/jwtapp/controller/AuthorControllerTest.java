package com.example.jwtapp.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.example.jwtapp.model.dto.AuthorDto;
import com.example.jwtapp.model.dto.BookDto;
import com.example.jwtapp.model.dto.BookRequest;
import com.example.jwtapp.service.AuthorService;
import com.example.jwtapp.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(AuthorController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthorControllerTest {

	@Autowired
	private MockMvc mockMvc;
	 
	@MockBean
	private AuthorService authorService;
	
	@MockBean
	private UserDetailsService userService;
	
	@MockBean
	private BookService bookService;

	private ObjectMapper objectMapper = new ObjectMapper();
	 
	 
	@Test
	@DisplayName("Given a successful save, response should give http status 200")
	public void shouldSaveAuthor() throws Exception {
		String name = "J. K. Rowling";
		
		AuthorDto expectedResponse = new AuthorDto();
		expectedResponse.setId(1L);
		expectedResponse.setName(name);
		
		Date today = new Date();
		expectedResponse.setCreatedDate(today);		
		expectedResponse.setUpdatedDate(today);

		when(authorService.createAuthor(name))
	    	.thenReturn(expectedResponse);
		
		Map<String, String> request = new HashMap();
		request.put("name", name);
	
		this.mockMvc.perform(post("/authors").content(
		            	objectMapper.writeValueAsString(request)
		    		).contentType(MediaType.APPLICATION_JSON))

			.andExpect(status().isOk())
			.andExpect(MockMvcResultMatchers.jsonPath("$.id").value("1"))
			.andExpect(MockMvcResultMatchers.jsonPath("$.name").value(name))
			.andExpect(MockMvcResultMatchers.jsonPath("$.createdDate").exists())
			.andExpect(MockMvcResultMatchers.jsonPath("$.updatedDate").exists());

		verify(authorService).createAuthor(name);
	}
	
	@Test
	@DisplayName("Should return author with correct details")
	public void shouldReturnAuthor() throws Exception {
		String name = "J. K. Rowling";
		
		AuthorDto expectedResponse = new AuthorDto();
		expectedResponse.setId(1L);
		expectedResponse.setName(name);
		
		Date today = new Date();
		expectedResponse.setCreatedDate(today);		
		expectedResponse.setUpdatedDate(today);

		when(authorService.getAuthor(1L))
	    	.thenReturn(expectedResponse);
		
		this.mockMvc.perform(get("/authors/1"))
			.andExpect(status().isOk())
			.andExpect(MockMvcResultMatchers.jsonPath("$.id").value("1"))
			.andExpect(MockMvcResultMatchers.jsonPath("$.name").value(name))
			.andExpect(MockMvcResultMatchers.jsonPath("$.createdDate").exists())
			.andExpect(MockMvcResultMatchers.jsonPath("$.updatedDate").exists());

		verify(authorService).getAuthor(1L);
	}
	
	@Test
	@DisplayName("Should return all authors with paging and sorting")
	public void shouldReturnAllAuthors() throws Exception {
		String name = "J. K. Rowling";
		
		AuthorDto expectedResponse = new AuthorDto();
		expectedResponse.setId(1L);
		expectedResponse.setName(name);
		
		Date today = new Date();
		expectedResponse.setCreatedDate(today);		
		expectedResponse.setUpdatedDate(today);

		Pageable pageable = PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC, "createdDate"));

		Page<AuthorDto> pagedAuthors = new PageImpl(Arrays.asList(expectedResponse));
		
		when(authorService.findAll(pageable))
	    	.thenReturn(pagedAuthors);
		
		this.mockMvc.perform(get("/authors?page=0&size=2&sort=createdDate,desc"))
			.andExpect(status().isOk())
			.andExpect(MockMvcResultMatchers.jsonPath("$.content.[0].id").value("1"))
			.andExpect(MockMvcResultMatchers.jsonPath("$.content.[0].name").value("J. K. Rowling"))
			.andExpect(MockMvcResultMatchers.jsonPath("$.content.[0].createdDate").exists())
			.andExpect(MockMvcResultMatchers.jsonPath("$.content.[0].updatedDate").exists());

		verify(authorService).findAll(pageable);
	}
	
	@Test
	public void AddBook() throws Exception {
		BookRequest request = new BookRequest("Harry Potter and the Sorcerer's Stone", "1st book");
		
		BookDto expectedResponse = new BookDto();
		expectedResponse.setId(1L);
		expectedResponse.setCreatedDate(new Date());
		expectedResponse.setUpdatedDate(new Date());
		expectedResponse.setTitle("Harry Potter and the Sorcerer's Stone");
		expectedResponse.setDescription("1st book");
		expectedResponse.setAuthorId(1L);
		
		when(bookService.addBook(1L, request))
    		.thenReturn(expectedResponse);
		
		this.mockMvc
			.perform(post("/authors/1/books")
			.content(objectMapper.writeValueAsString(request))
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(MockMvcResultMatchers.jsonPath("$.id").value("1"))
			.andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Harry Potter and the Sorcerer's Stone"))
			.andExpect(MockMvcResultMatchers.jsonPath("$.description").value("1st book"))
			.andExpect(MockMvcResultMatchers.jsonPath("$.authorId").value("1"))
			.andExpect(MockMvcResultMatchers.jsonPath("$.createdDate").exists())
			.andExpect(MockMvcResultMatchers.jsonPath("$.updatedDate").exists());

		verify(bookService).addBook(1L, request);
	}
	
	@Test
	@DisplayName("Get all books by author with paging and sorting")
	public void getBooksByAuthorWithPagingAndSorting() throws Exception {
		BookDto book1 = new BookDto();
		book1.setId(1L);
		book1.setCreatedDate(new Date());
		book1.setUpdatedDate(new Date());
		book1.setTitle("Harry Potter and the Sorcerer's Stone");
		book1.setDescription("1st book");
		book1.setAuthorId(1L);
		
		BookDto book2 = new BookDto();
		book2.setId(2L);
		book2.setCreatedDate(new Date());
		book2.setUpdatedDate(new Date());
		book2.setTitle("Harry Potter and the Chamber of Secrets");
		book2.setDescription("2nd book");
		book2.setAuthorId(1L);
		
		
		Pageable pageable = PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC, "createdDate"));

		Page<BookDto> pagedAuthors = new PageImpl(Arrays.asList(book1, book2));
		
		when(bookService.getBooks(1L, pageable))
	    	.thenReturn(pagedAuthors);
		
		this.mockMvc.perform(get("/authors/1/books?page=0&size=2&sort=createdDate,desc"))
			.andExpect(status().isOk())
			.andExpect(MockMvcResultMatchers.jsonPath("$.content.[0].id").value("1"))
			.andExpect(MockMvcResultMatchers.jsonPath("$.content.[0].title").value("Harry Potter and the Sorcerer's Stone"))
			.andExpect(MockMvcResultMatchers.jsonPath("$.content.[0].description").value("1st book"))
			.andExpect(MockMvcResultMatchers.jsonPath("$.content.[1].id").value("2"))
			.andExpect(MockMvcResultMatchers.jsonPath("$.content.[1].title").value("Harry Potter and the Chamber of Secrets"))
			.andExpect(MockMvcResultMatchers.jsonPath("$.content.[1].description").value("2nd book"));
		
		verify(bookService).getBooks(1L, pageable);
	}
	
	@Test
	@DisplayName("Get book by id")
	public void getBookById() throws Exception {
	    BookDto book = new BookDto();
	    book.setId(1L);
	    book.setTitle("Harry Potter and the Sorcerer's Stone");
	    book.setDescription("1st book");
	    book.setCreatedDate(new Date());
	    book.setUpdatedDate(new Date());
	    book.setAuthorId(1L);
	     
		Page<BookDto> pagedBook = new PageImpl(Arrays.asList(book));

		Pageable pageable = PageRequest.of(0, 1);

		when(bookService.getBook(1L, 1L, pageable))
			.thenReturn(pagedBook);
	
		this.mockMvc.perform(get("/authors/1/books/1"))
			.andExpect(status().isOk())
			.andExpect(MockMvcResultMatchers.jsonPath("$.content.[0].id").value("1"))
			.andExpect(MockMvcResultMatchers.jsonPath("$.content.[0].title").value("Harry Potter and the Sorcerer's Stone"))
			.andExpect(MockMvcResultMatchers.jsonPath("$.content.[0].description").value("1st book"))
			.andExpect(MockMvcResultMatchers.jsonPath("$.content.[0].authorId").value("1"))
			.andExpect(MockMvcResultMatchers.jsonPath("$.content.[0].createdDate").exists())
			.andExpect(MockMvcResultMatchers.jsonPath("$.content.[0].updatedDate").exists());

		verify(bookService).getBook(1L, 1L, pageable);
	}		  
}
