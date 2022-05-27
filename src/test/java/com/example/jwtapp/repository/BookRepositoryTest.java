package com.example.jwtapp.repository;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.example.jwtapp.model.Author;
import com.example.jwtapp.model.Book;
import com.example.jwtapp.model.dto.BookDto;
import com.example.jwtapp.model.dto.BookRequest;

@EnableJpaAuditing
@DataJpaTest
public class BookRepositoryTest {
	
	@Autowired
	private AuthorRepository authorRepository;

	@Autowired
	private BookRepository bookRepository;
	
	
	@Test
	@DisplayName("Create a book and add to author")
	public void CreateBookAndaddToAuthor() {
		Author newAuthor = new Author();
		newAuthor.setId(1L);
		
		Author savedAuthor = authorRepository.save(newAuthor);

		Book newBook = new Book();
		newBook.setTitle("Harry Potter and the Sorcerer's Stone");
		newBook.setDescription("1st book");
		
		savedAuthor.addBooks(newBook);
		
		Book actualResponse = bookRepository.save(newBook);
		
		assertAll(
			() -> assertNotNull(actualResponse.getCreatedDate()),
	        () -> assertNotNull(actualResponse.getUpdatedDate()),	            
			() -> assertNotNull(actualResponse.getId()),
	        () -> assertEquals(newBook.getTitle(), actualResponse.getTitle()),
	        () -> assertEquals(newBook.getDescription(), actualResponse.getDescription()),
	        () -> assertEquals(newBook.getAuthor().getId(), actualResponse.getAuthor().getId())
	    );		
	}
	
	@Test
	@DisplayName("Get books with paging and sorting")
	public void getBooksWithPagingAndSorting() {
		Author newAuthor = new Author();
	    newAuthor.setName("J. K. Rowling");
	     
	    Book book1 = new Book();
	    book1.setTitle("Harry Potter and the Sorcerer's Stone");
	    book1.setAuthor(newAuthor);

	    Book book2 = new Book();
	    book2.setTitle("Harry Potter and the Chamber of Secrets");
	    book2.setAuthor(newAuthor);
	     
	    newAuthor.addBooks(book1, book2);
	    	     
	    Author savedAuthor = authorRepository.save(newAuthor);	
	     
	 	int page = 0;
		int size = 5;
		String sort = "title";
	
	 	Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, sort));

	    Page<Book> pagedBooks = bookRepository.findByAuthorId(savedAuthor.getId(), pageable);
	    
	 	assertAll(
			   () -> assertEquals(2, pagedBooks.getNumberOfElements()),
			   () -> assertEquals("Harry Potter and the Sorcerer's Stone", pagedBooks.getContent().get(0).getTitle()),			    
			   () -> assertEquals("Harry Potter and the Chamber of Secrets", pagedBooks.getContent().get(1).getTitle())	   
	 		);		
	}	
	
	@Test
	@DisplayName("Get a book by id and author id")
	public void getBookById() {
		Author newAuthor = new Author();
	    newAuthor.setName("J. K. Rowling");
	     
	    Book book1 = new Book();
	    book1.setTitle("Harry Potter and the Sorcerer's Stone");
	    book1.setAuthor(newAuthor);

	    Book book2 = new Book();
	    book2.setTitle("Harry Potter and the Chamber of Secrets");
	    book2.setAuthor(newAuthor);
	     
	    newAuthor.addBooks(book1, book2);
	    	     
	    Author savedAuthor = authorRepository.save(newAuthor);	
	     
	 	Pageable pageable = PageRequest.of(0, 1);

	  	Long bookId = savedAuthor.getBooks().stream().findFirst().map(b -> b.getId()).orElse(0L);
	 	Page<Book> pagedBook = bookRepository.findByIdAndAuthorId(bookId, savedAuthor.getId(), pageable);
	    
	 	Book book = pagedBook.getContent().get(0);
	 	
	 	assertEquals(1, pagedBook.getNumberOfElements());
	 	assertAll(
			   () -> assertNotNull(book),
			   () -> assertNotNull(book.getTitle()),
			   () -> assertNotNull(book.getCreatedDate()),			    
			   () -> assertNotNull(book.getUpdatedDate())			    
	 		);		
	}	
	
	@Test
	@DisplayName("Get a book by title")
	public void getBookByTitle() {
		Author newAuthor = new Author();
	    newAuthor.setName("J. K. Rowling");
	     
	    Book book1 = new Book();
	    book1.setTitle("Harry Potter and the Sorcerer's Stone");
	    book1.setAuthor(newAuthor);

	    Book book2 = new Book();
	    book2.setTitle("Harry Potter and the Chamber of Secrets");
	    book2.setAuthor(newAuthor);
	     
	    newAuthor.addBooks(book1, book2);
	    	     
	    authorRepository.save(newAuthor);	
	 
	 	Pageable pageable = PageRequest.of(0, 10);

	 	Page<Book> pagedBooks = bookRepository.findByTitleContains("Harry Potter", pageable);
	    
	 	List<Book> books = pagedBooks.getContent();
	 	
	 	assertEquals(2, books.size());
	 	assertAll(
			() -> assertNotNull(books.get(0)),
			() -> assertNotNull(books.get(0).getTitle()),
			() -> assertNotNull(books.get(0).getCreatedDate()),			    
			() -> assertNotNull(books.get(0).getUpdatedDate())			    
	 	);		
	 	
	 	assertAll(
			() -> assertNotNull(books.get(1)),
			() -> assertNotNull(books.get(1).getTitle()),
			() -> assertNotNull(books.get(1).getCreatedDate()),			    
			() -> assertNotNull(books.get(1).getUpdatedDate())			    
		 );	
	}	
}
