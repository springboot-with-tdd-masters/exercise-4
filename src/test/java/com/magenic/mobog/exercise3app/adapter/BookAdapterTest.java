package com.magenic.mobog.exercise3app.adapter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.magenic.mobog.exercise3app.entities.Author;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.magenic.mobog.exercise3app.adapters.BookAdapter;
import com.magenic.mobog.exercise3app.entities.Book;
import com.magenic.mobog.exercise3app.exceptions.InvalidRequestException;
import com.magenic.mobog.exercise3app.requests.AddBookRequest;
import com.magenic.mobog.exercise3app.responses.BookResponse;

import java.time.LocalDateTime;

class BookAdapterTest {
	
	private BookAdapter bookAdapter;
	
	@BeforeEach
	void setup() {
		bookAdapter = new BookAdapter();
	}

	
	@Test
	@DisplayName("should map BookResponse to BookEntity")
	void shouldMapBookRequestToBookEntity() {
		// given
		AddBookRequest request = new AddBookRequest();
		request.setTitle("Catwoman");
		
		// when
		Book actual = bookAdapter.mapRequestToEntity(request);
		
		// then
		assertEquals("Catwoman", actual.getTitle());
	}
	@Test
	@DisplayName("should map BookEntity to BookResponse")
	void shouldMapBookEntityToBookResponse() {
		// given
		Book entity = new Book();
		Author author = new Author();
		author.setId(1L);
		author.setName("Paul Dano");
		entity.setAuthor(author);
		entity.setTitle("There Will Be Riddler");
		entity.setId(1L);
		entity.setCreatedDate(LocalDateTime.now());
		entity.setLastModifiedDate(LocalDateTime.now());
		
		// when
		BookResponse actual = bookAdapter.mapBookToResponse(entity);
		
		// then
		assertEquals(1L, actual.getId());
		assertEquals(1L, actual.getAuthorId());
		assertEquals("There Will Be Riddler", actual.getTitle());
	}
	@Test
	@DisplayName("should throw exception if author or title is empty")
	void shouldThrowExceptionOnWhitespaces() {
		// given
		AddBookRequest request = new AddBookRequest();
		assertThrows(InvalidRequestException.class, () -> bookAdapter.mapRequestToEntity(request));
	}
}
