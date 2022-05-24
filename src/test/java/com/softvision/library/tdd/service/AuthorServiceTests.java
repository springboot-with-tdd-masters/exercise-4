package com.softvision.library.tdd.service;

import com.softvision.library.tdd.model.Author;
import com.softvision.library.tdd.model.Book;
import com.softvision.library.tdd.model.RecordNotFoundException;
import com.softvision.library.tdd.repository.AuthorRepository;
import com.softvision.library.tdd.repository.BookRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class AuthorServiceTests {

    @Mock
    AuthorRepository mockAuthorRepository;
    @Mock
    BookRepository mockBookRepository;

    @Mock
    Pageable mockPageable;
    @Mock
    Page<Author> mockAuthorsPage;
    @Mock
    Author mockAuthor;
    @Mock
    Author mockAuthorExpected;
    @Mock
    Book mockBook;

    static final String MOCK_CONTAINS = "Sun";

    @InjectMocks
    AuthorService authorService = new AuthorServiceImpl();

    @Test
    @DisplayName("Create - should create an author")
    void test_create() {
        when(mockAuthorRepository.save(mockAuthor)).thenReturn(mockAuthorExpected);
        assertSame(mockAuthorExpected, authorService.create(mockAuthor));

        verify(mockAuthorRepository, atMostOnce()).save(any());
    }

    @Test
    @DisplayName("Get All - should return a paged author")
    void test_getAll() {
        when(mockAuthorRepository.findAll(mockPageable)).thenReturn(mockAuthorsPage);
        assertSame(mockAuthorsPage, authorService.getAll(mockPageable));

        verify(mockAuthorRepository, atMostOnce()).findAll(any(Pageable.class));
    }

    @Test
    @DisplayName("Get By Id - should return an author")
    void test_getById() {
        when(mockAuthorRepository.findById(1L)).thenReturn(Optional.of(mockAuthorExpected));
        assertSame(mockAuthorExpected, authorService.getById(1L));

        verify(mockAuthorRepository, atMostOnce()).findById(anyLong());
    }

    @Test
    @DisplayName("Get By Id - should throw RecordNotFoundException when id is not found")
    void test_getById_throwExceptionWhenEmpty() {
        when(mockAuthorRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RecordNotFoundException.class, () -> authorService.getById(1L));

        verify(mockAuthorRepository, atMostOnce()).findById(anyLong());
    }

    @Test
    @DisplayName("Get Contains - should return authors with name 'Sun'")
    void test_getContains() {
        List<Author> mockPageContent = List.of(mockAuthorExpected);
        when(mockAuthorRepository.findByNameContaining(MOCK_CONTAINS, mockPageable)).thenReturn(new PageImpl<>(mockPageContent));
        Page<Author> actualAuthorPage = authorService.getContainingName(MOCK_CONTAINS, mockPageable);
        assertEquals(mockPageContent, actualAuthorPage.getContent());

        verify(mockAuthorRepository, atMostOnce()).findByNameContaining(any(), any());
    }

    @Test
    @DisplayName("Get Contains - should throw RecordNotFoundException when repository returns empty")
    void test_getContains_throwExceptionWhenEmpty() {
        when(mockAuthorRepository.findByNameContaining(MOCK_CONTAINS, mockPageable)).thenReturn(new PageImpl<>(Collections.emptyList()));
        assertThrows(RecordNotFoundException.class, () -> authorService.getContainingName(MOCK_CONTAINS, mockPageable));

        verify(mockAuthorRepository, atMostOnce()).findByNameContaining(any(), any());
    }

    @Test
    @DisplayName("Create Book - should create a book")
    void test_createBook() {
        when(mockAuthorRepository.findById(1L)).thenReturn(Optional.of(mockAuthorExpected));
        when(mockBookRepository.save(mockBook)).thenReturn(mockBook);

        assertSame(mockBook, authorService.createBook(1L, mockBook));

        verify(mockAuthorRepository, atMostOnce()).findById(anyLong());
        verify(mockAuthorRepository, atMostOnce()).save(any());
    }

    @Test
    @DisplayName("Create Book - should throw RecordNotFoundException when id is not found")
    void test_createBook_throwException() {
        when(mockAuthorRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RecordNotFoundException.class, () -> authorService.createBook(1L, mockBook));

        verify(mockAuthorRepository, atMostOnce()).findById(anyLong());
    }
}
