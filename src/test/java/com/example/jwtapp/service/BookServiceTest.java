package com.example.jwtapp.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
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

import com.example.jwtapp.model.Author;
import com.example.jwtapp.model.Book;
import com.example.jwtapp.model.dto.BookDto;
import com.example.jwtapp.model.dto.BookRequest;
import com.example.jwtapp.repository.AuthorRepository;
import com.example.jwtapp.repository.BookRepository;

public class BookServiceTest {
	
	@Mock
	private BookRepository bookRepository;
	
	@Mock
	private AuthorRepository authorRepository;
	
	@InjectMocks
	private BookServiceImpl bookService;
	
	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}
	
	@Test
	@DisplayName("Create a book and add to author")
	public void CreateBookAndaddToAuthor() {
		Author author = new Author();
		author.setId(1L);
		
		when(authorRepository.findById(1L))
			.thenReturn(Optional.of(author));
		
		Book savedBook = new Book();
		savedBook.setId(1L);
		savedBook.setTitle("Harry Potter and the Sorcerer's Stone");
		savedBook.setDescription("1st book");
		savedBook.setCreatedDate(new Date());
		savedBook.setUpdatedDate(new Date());
		author.addBooks(savedBook);

		Book newBook = new Book();
		newBook.setTitle("Harry Potter and the Sorcerer's Stone");
		newBook.setDescription("1st book");

		author.addBooks(newBook);

		when(bookRepository.save(newBook))
			.thenReturn(savedBook);
	
		BookRequest request = new BookRequest("Harry Potter and the Sorcerer's Stone", "1st book");
		
		BookDto actualResponse = bookService.addBook(1L, request);
			
		assertAll(
			() -> assertNotNull(actualResponse.getCreatedDate()),
	        () -> assertNotNull(actualResponse.getUpdatedDate()),	            
			() -> assertNotNull(actualResponse.getId()),
	        () -> assertEquals("Harry Potter and the Sorcerer's Stone", actualResponse.getTitle()),
	        () -> assertEquals("1st book", actualResponse.getDescription()),
	        () -> assertEquals(1, actualResponse.getAuthorId())
	    );		
	}
	
	@Test
	public void getBooksWithPagingAndSorting() {
		Author newAuthor = new Author();
	    newAuthor.setName("J. K. Rowling");
	     
	    Book book1 = new Book();
	    book1.setTitle("Harry Potter and the Sorcerer's Stone");
	    book1.setAuthor(newAuthor);

	    Book book2 = new Book();
	    book2.setTitle("Harry Potter and the Chamber of Secrets");
	    book2.setAuthor(newAuthor);
	     
		Page<Book> books = new PageImpl(Arrays.asList(book1, book2));

		Pageable pageable = PageRequest.of(0, 20, Sort.by(Sort.Direction.DESC, "title"));

	 	when(bookRepository.findByAuthorId(1L, pageable))
	 		.thenReturn(books);

	    Page<BookDto> pagedBooks = bookService.getBooks(1L, pageable);
	    
	 	assertAll(
			   () -> assertEquals(2, pagedBooks.getNumberOfElements()),
			   () -> assertEquals("Harry Potter and the Sorcerer's Stone", pagedBooks.getContent().get(0).getTitle()),			    
			   () -> assertEquals("Harry Potter and the Chamber of Secrets", pagedBooks.getContent().get(1).getTitle())	   
	 		);		
	}
	
	@Test
	public void getBookById() {
		Author newAuthor = new Author();
	    newAuthor.setName("J. K. Rowling");
	     
	    Book book1 = new Book();
	    book1.setId(1L);
	    book1.setTitle("Harry Potter and the Sorcerer's Stone");
	    book1.setCreatedDate(new Date());
	    book1.setAuthor(newAuthor);
	     
		Page<Book> books = new PageImpl(Arrays.asList(book1));

		Pageable pageable = PageRequest.of(0, 1);

		when(bookRepository.findByIdAndAuthorId(1L, 1L, pageable))
			.thenReturn(books);
		
	 	Page<BookDto> pagedBook = bookService.getBook(1L, 1L, pageable);
	    
	 	BookDto book = pagedBook.getContent().get(0);
	 	assertAll(
			   () -> assertNotNull(book),
			   () -> assertNotNull(book.getTitle()),
			   () -> assertNotNull(book.getCreatedDate())		    
	 	);
	}
	
	
	@Test
	public void getBooksByTitle() {
		Author author = new Author();
		author.setId(1L);
		author.setName("J. K. Rowling");
		
		Book book1 = new Book();
	    book1.setTitle("Harry Potter and the Sorcerer's Stone");
	    book1.setCreatedDate(new Date());
	    book1.setAuthor(author);
	   
	    Book book2 = new Book();
	    book2.setTitle("Harry Potter and the Chamber of Secrets");
	    book2.setCreatedDate(new Date());
	    book2.setAuthor(author);

		Page<Book> books = new PageImpl(Arrays.asList(book1, book2));
		
	 	Pageable pageable = PageRequest.of(0, 20);

		when(bookRepository.findByTitleContains("Harry Potter", pageable))
			.thenReturn(books);
		
		Page<BookDto> pagedBooks = bookService.getBooks("Harry Potter");
		
		List<BookDto> bookList = pagedBooks.getContent();
		
		assertEquals(2, pagedBooks.getNumberOfElements());
		
	 	assertAll(
			() -> assertNotNull(bookList.get(0)),
			() -> assertNotNull(bookList.get(0).getTitle()),
			() -> assertNotNull(bookList.get(0).getCreatedDate())		    
	 	);		
	 	
	 	assertAll(
			() -> assertNotNull(bookList.get(1)),
			() -> assertNotNull(bookList.get(1).getTitle()),
			() -> assertNotNull(bookList.get(1).getCreatedDate())		    
		 );	
	}
}
