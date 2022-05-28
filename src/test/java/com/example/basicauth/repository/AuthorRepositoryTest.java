package com.example.basicauth.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.example.basicauth.model.Author;
import com.example.basicauth.model.Book;

@EnableJpaAuditing
@DataJpaTest
public class AuthorRepositoryTest {
	
	@Autowired
	private AuthorRepository authorRepository;

	@Test
	@DisplayName("Should save author entity")
	public void saveAuthor() {
	     Author author = new Author();
	     author.setName("J.K. Rowling");

	     Author savedAuthor = authorRepository.save(author);

	     assertEquals(author.getName(), savedAuthor.getName());
	     assertNotNull(savedAuthor.getCreatedDate());
	     assertNotNull(savedAuthor.getUpdatedDate());
	}
	
	@Test
	@DisplayName("Should save author with books")
	public void saveAuthorWithBooks() {
	     Author newAuthor = new Author();
	     newAuthor.setName("J.K. Rowling");
	     
	     Book book1 = new Book();
	     book1.setTitle("Harry Potter and the Sorcerer's Stone");
	     book1.setAuthor(newAuthor);

	     Book book2 = new Book();
	     book2.setTitle("Harry Potter and the Chamber of Secrets");
	     book2.setAuthor(newAuthor);
	     
	     newAuthor.addBooks(book1, book2);
	     
	     Author savedAuthor = authorRepository.save(newAuthor);

	     assertEquals(newAuthor.getName(), savedAuthor.getName());
	     assertThat(savedAuthor.getBooks())
	     	.hasSize(2)
	     	.contains(book1, book2);
	}
	
	@Test
	@DisplayName("Get author with books")
	public void getAuthorWithBooks() {
	     Author newAuthor = new Author();
	     newAuthor.setName("J.K. Rowling");
	     
	     Book book1 = new Book();
	     book1.setTitle("Harry Potter and the Sorcerer's Stone");
	     book1.setAuthor(newAuthor);

	     Book book2 = new Book();
	     book2.setTitle("Harry Potter and the Chamber of Secrets");
	     book2.setAuthor(newAuthor);
	     
	     newAuthor.addBooks(book1, book2);
	     
	     Author savedAuthor = authorRepository.save(newAuthor);
	     
	     Author author = authorRepository.findById(savedAuthor.getId()).get();
	     
	     assertEquals(newAuthor.getName(), author.getName());
	     assertThat(author.getBooks())
	     	.hasSize(2)
	     	.contains(book1, book2);
	}
	
	@Test
	@DisplayName("Get authors with paging and sorting")
	public void getPaginatedAuthorListSortedByName() {
		Author author1 = new Author();
	    author1.setName("J. K. Rowling");
	     
	    Author author2 = new Author();
	    author2.setName("J. R. R. Tolkien");
	     
	    Author author3 = new Author();
	    author3.setName("George Orwell");
	    
	    Author author4 = new Author();
	    author4.setName("William Shakespeare");
	    	     
	    authorRepository.save(author1);	
	    authorRepository.save(author2);	
	    authorRepository.save(author3);	
	    authorRepository.save(author4);
	     
	 	int page = 0;
		int size = 3;
		String sort = "name";
	
	 	Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, sort));

	    Page<Author> pagedAuthors = authorRepository.findAll(pageable);
	    
	 	assertAll(
	 		() -> assertEquals(2, pagedAuthors.getTotalPages()),
			() -> assertEquals(4, pagedAuthors.getTotalElements()),
			() -> assertEquals(3, pagedAuthors.getNumberOfElements()),
			() -> assertEquals("William Shakespeare", pagedAuthors.getContent().get(0).getName()),			   
			() -> assertEquals("J. R. R. Tolkien", pagedAuthors.getContent().get(1).getName()),			    
			() -> assertEquals("J. K. Rowling", pagedAuthors.getContent().get(2).getName())
	 	);		
	}	
}
