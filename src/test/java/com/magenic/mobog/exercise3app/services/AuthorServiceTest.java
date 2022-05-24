package com.magenic.mobog.exercise3app.services;

import com.magenic.mobog.exercise3app.adapters.AuthorAdapter;
import com.magenic.mobog.exercise3app.adapters.BookAdapter;
import com.magenic.mobog.exercise3app.entities.Author;
import com.magenic.mobog.exercise3app.entities.Book;
import com.magenic.mobog.exercise3app.exceptions.EntityNotFoundException;
import com.magenic.mobog.exercise3app.exceptions.InvalidRequestException;
import com.magenic.mobog.exercise3app.repositories.AuthorRepository;
import com.magenic.mobog.exercise3app.repositories.BookRepository;
import com.magenic.mobog.exercise3app.requests.AddBookRequest;
import com.magenic.mobog.exercise3app.requests.AddAuthorRequest;
import com.magenic.mobog.exercise3app.responses.AuthorResponse;
import com.magenic.mobog.exercise3app.responses.BookResponse;
import com.magenic.mobog.exercise3app.responses.PageResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthorServiceTest {

    @Mock
    BookRepository bookRepository;
    @Mock
    AuthorRepository authorRepository;

    AuthorAdapter authorAdapter;

    BookAdapter bookAdapter;

    AuthorService service;

    @BeforeEach
    void setup(){
        authorAdapter = new AuthorAdapter();
        bookAdapter = new BookAdapter();
        service = new AuthorService(authorRepository, bookRepository,authorAdapter, bookAdapter);
    }
    @Test
    @DisplayName("should return a correct response for createAuthor")
    void shouldReturnCorrectResponseForCreateAuthor(){
        // given
        AddAuthorRequest request = new AddAuthorRequest();
        request.setName("Johnny Bravo");
        Author saved = new Author();
        saved.setId(1L);
        saved.setName("Johnny Bravo");
        saved.setCreatedDate(LocalDateTime.now());
        saved.setLastModifiedDate(LocalDateTime.now());
        when(authorRepository.save(any(Author.class))).thenReturn(saved);
        // when
        AuthorResponse actual = service.createAuthor(request);
        // then
        assertEquals(1L, actual.getId());
        assertNotNull(actual.getCreatedAt());
        assertNotNull(actual.getUpdatedAt());
        assertEquals("Johnny Bravo",actual.getName());
    }
    @Test
    @DisplayName("should return a correct exception for createAuthor")
    void shouldReturnCorrectExceptionForCreateAuthor(){
        AddAuthorRequest request = new AddAuthorRequest();
        request.setName("Johnny Bravo");
        // when
        assertThrows(InvalidRequestException.class,() -> service.createAuthor(request));
        // then
    }

    @Test
    @DisplayName("should return a correct response for addBookToAuthor")
    void shouldReturnCorrectResponseWhenAddBookIsCalled(){
        // given
        Author dummyAuthor = new Author();
        dummyAuthor.setId(1L);
        dummyAuthor.setName("Jane Doe");
        when(authorRepository.findById(1L)).thenReturn(Optional.of(dummyAuthor));

        Book saved = new Book();
        saved.setAuthor(dummyAuthor);
        saved.setTitle("Divine Tempest");
        saved.setId(1L);
        saved.setCreatedDate(LocalDateTime.now());
        saved.setLastModifiedDate(LocalDateTime.now());
        when(bookRepository.save(any())).thenReturn(saved);
        // when
        AddBookRequest request = new AddBookRequest();
        request.setTitle("Divine Tempest");
        BookResponse actual = service.addBookToAuthor(1L, request);
        // then
        assertEquals("Divine Tempest", actual.getTitle());
        assertEquals(1L, actual.getAuthorId());
        assertEquals(1L, actual.getId());
        assertNotNull(actual.getCreatedAt());
        assertNotNull(actual.getUpdatedAt());
    }
    @Test
    @DisplayName("should return a correct exception if author not found")
    void shouldReturnCorrectExceptionForAuthorNotFound(){
        // given
        when(authorRepository.findById(1L)).thenReturn(Optional.empty());
        // when
        AddBookRequest request = new AddBookRequest();
        request.setTitle("Divine Tempest");
        // then
        assertThrows(EntityNotFoundException.class, () -> service.addBookToAuthor(1L, request));
    }
    @Test
    @DisplayName("should return a correct exception if unable to save book.")
    void shouldReturnCorrectExceptionForException(){
        // given
        when(authorRepository.findById(1L)).thenReturn(Optional.of(new Author()));
        // when
        AddBookRequest request = new AddBookRequest();
        request.setTitle("Divine Tempest");
        // then
        assertThrows(InvalidRequestException.class, () -> service.addBookToAuthor(1L, request));
    }
    @Test
    @DisplayName("should return a page with correct content for findAuthorsWithPage")
    void shouldReturnPageWithCorrectContentForFindAuthors(){
        // given
        Pageable request = PageRequest.of(0, 1);
        Author author1 = new Author();
        author1.setName("Adam");
        author1.setLastModifiedDate(LocalDateTime.now());
        author1.setCreatedDate(LocalDateTime.now());
        Author author2 = new Author();
        author2.setName("Eve");
        author2.setLastModifiedDate(LocalDateTime.now());
        author2.setCreatedDate(LocalDateTime.now());
        Author author3 = new Author();
        author3.setName("Cain");
        author3.setLastModifiedDate(LocalDateTime.now());
        author3.setCreatedDate(LocalDateTime.now());
        Page<Author> page = mock(Page.class);
        when(page.get()).thenReturn(Stream.of(author1));
        when(page.getPageable()).thenReturn(request);
        when(page.getTotalPages()).thenReturn(3);
        // mock
        when(authorRepository.findAll(any(Pageable.class))).thenReturn(page);
        // when
        PageResponse<AuthorResponse> actual = service.findAuthorsWithPage(request);
        // then
        AuthorResponse expected = new AuthorResponse();
        expected.setName(author1.getName());
        expected.setId(author1.getId());
        expected.setCreatedAt(author1.getCreatedDate().toString());
        expected.setUpdatedAt(author1.getLastModifiedDate().toString());
        assertEquals(1, actual.getSize());
        assertEquals(List.of(expected), actual.getContent());
        assertEquals(0, actual.getPage());
        assertEquals(3, actual.getTotalPage());
    }
    @Test
    @DisplayName("should return a page with empty content for findAuthorsWithPage")
    void shouldReturnPageWithEmptyContentForFindAuthors(){
        // given
        Pageable request = PageRequest.of(2, 2);
        Page<Author> page = mock(Page.class);
        when(page.get()).thenReturn(Stream.empty());
        when(page.getPageable()).thenReturn(request);
        when(page.getTotalPages()).thenReturn(3);
        // mock
        when(authorRepository.findAll(any(Pageable.class))).thenReturn(page);
        // when
        PageResponse<AuthorResponse> actual = service.findAuthorsWithPage(request);
        // then
        assertEquals(2, actual.getSize());
        assertEquals(new ArrayList<>(), actual.getContent());
        assertEquals(2, actual.getPage());
        assertEquals(3, actual.getTotalPage());
    }

    @Test
    @DisplayName("should return a page with correct content for findBookByAuthorWithPage")
    void shouldReturnPageWithCorrectContentForFindBookByAuthors(){
        // given
        Pageable request = PageRequest.of(0, 1);
        Author author = new Author();
        author.setId(1L);
        Book book1 = new Book();
        book1.setAuthor(author);
        book1.setTitle("Divine Tempest");
        book1.setCreatedDate(LocalDateTime.now());
        book1.setLastModifiedDate(LocalDateTime.now());
        Page<Book> page = mock(Page.class);
        when(page.get()).thenReturn(Stream.of(book1));
        when(page.getPageable()).thenReturn(request);
        when(page.getTotalPages()).thenReturn(3);
        // mock
        when(bookRepository.findByAuthorId(anyLong(), any(Pageable.class))).thenReturn(page);
        // when
        PageResponse<BookResponse> actual = service.findBookByAuthorWithPage(1L, request);
        // then
        BookResponse expected = new BookResponse();
        expected.setTitle(book1.getTitle());
        expected.setId(book1.getId());
        expected.setAuthorId(book1.getAuthor().getId());
        expected.setCreatedAt(book1.getCreatedDate().toString());
        expected.setUpdatedAt(book1.getLastModifiedDate().toString());
        assertEquals(1, actual.getSize());
        assertEquals(List.of(expected), actual.getContent());
        assertEquals(0, actual.getPage());
        assertEquals(3, actual.getTotalPage());
    }
    @Test
    @DisplayName("should return a page with empty content for findBookByAuthorWithPage")
    void shouldReturnPageWithEmptyContentForFindBookByAuthors(){
        // given
        Pageable request = PageRequest.of(2, 2);
        Page<Book> page = mock(Page.class);
        when(page.get()).thenReturn(Stream.empty());
        when(page.getPageable()).thenReturn(request);
        when(page.getTotalPages()).thenReturn(3);
        // mock
        when(bookRepository.findByAuthorId(anyLong(), any(Pageable.class))).thenReturn(page);
        // when
        PageResponse<BookResponse> actual = service.findBookByAuthorWithPage(1L, request);
        // then
        assertEquals(2, actual.getSize());
        assertEquals(new ArrayList<>(), actual.getContent());
        assertEquals(2, actual.getPage());
        assertEquals(3, actual.getTotalPage());
    }
    @Test
    @DisplayName("should return a page with empty content for findByTitleContaining")
    void shouldReturnPageWithEmptyContentForFindBookContaingTitle(){
        // given
        Pageable request = PageRequest.of(2, 2);
        Page<Book> page = mock(Page.class);
        when(page.get()).thenReturn(Stream.empty());
        when(page.getPageable()).thenReturn(request);
        when(page.getTotalPages()).thenReturn(3);
        // mock
        when(bookRepository.findByTitleContaining(anyString(), any(Pageable.class))).thenReturn(page);
        // when
        PageResponse<BookResponse> actual = service.findBooksContainingTitle("stub", request);
        // then
        assertEquals(2, actual.getSize());
        assertEquals(new ArrayList<>(), actual.getContent());
        assertEquals(2, actual.getPage());
        assertEquals(3, actual.getTotalPage());
    }
    @Test
    @DisplayName("should return book response")
    void shouldReturnBookResponse(){
        Author author = new Author();
        author.setId(23L);
        Book entity = new Book();
        entity.setId(1L);
        entity.setAuthor(author);
        entity.setCreatedDate(LocalDateTime.now());
        entity.setLastModifiedDate(LocalDateTime.now());
        entity.setTitle("The Prophet");

        when(bookRepository.findByIdAndAuthorId(any(), any())).thenReturn(Optional.of(entity));
        // when
        BookResponse actual = this.service.getBookMadeByAuthor(1L, 23L);
        // then
        assertEquals(actual.getAuthorId(), 23L);
        assertEquals(actual.getTitle(), "The Prophet");
        assertEquals(actual.getId(), 1L);
    }
    @Test
    @DisplayName("should return author response")
    void shouldReturnAuthorResponse(){
        Author author = new Author();
        author.setId(23L);
        author.setName("Han Nibal");
        author.setCreatedDate(LocalDateTime.now());
        author.setLastModifiedDate(LocalDateTime.now());

        when(authorRepository.findById(any())).thenReturn(Optional.of(author));
        // when
        AuthorResponse actual = this.service.getAuthorById(1L);
        // then
        assertEquals(actual.getId(), 23L);
        assertEquals(actual.getName(), "Han Nibal");
    }
    @Test
    @DisplayName("should throw EntityNotFoundException if not found")
    void shouldThrowEntityNotFoundException(){
        Author author = new Author();
        author.setId(23L);
        author.setName("Han Nibal");
        author.setCreatedDate(LocalDateTime.now());
        author.setLastModifiedDate(LocalDateTime.now());

        when(authorRepository.findById(any())).thenThrow(new EntityNotFoundException());
        // when
        // then
      assertThrows(EntityNotFoundException.class, () -> this.service.getAuthorById(1L));
    }
}
