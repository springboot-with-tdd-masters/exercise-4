package com.softvision.library.tdd.service;

import com.softvision.library.tdd.model.Book;
import com.softvision.library.tdd.model.RecordNotFoundException;
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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.softvision.library.tdd.LibraryMocks.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTests {

    static final String[] PROPERTIES_TO_EXTRACT = {"title", "author.name"};

    @Mock
    BookRepository mockBookRepository;

    @Mock
    Pageable mockPageable;
    @Mock
    Page<Book> mockBookPage;

    static final String MOCK_TITLE_CONTAINS = "The";

    @InjectMocks
    BookService bookService = new BookServiceImpl();

    @Test
    @DisplayName("Create - should create a Book")
    void test_create() {
        final Book book = getMockBook2();
        book.setAuthor(getMockAuthor2());
        when(mockBookRepository.save(book)).thenReturn(book);

        assertThat(bookService.createOrUpdate(book))
                .extracting(PROPERTIES_TO_EXTRACT)
                .containsExactly(MOCK_TITLE_TP, MOCK_AUTHOR_NM);

        verify(mockBookRepository, atMostOnce()).findById(anyLong());
        verify(mockBookRepository, atMostOnce()).save(any());
    }

    @Test
    @DisplayName("Update - should update a found Book")
    void test_update() {
        final Book savedBookWithTypo = new Book("Thx Arts oF War");
        savedBookWithTypo.setId(MOCK_BOOK_ID_1);

        final Book fixedBook = getMockBook1();
        fixedBook.setId(MOCK_BOOK_ID_1);
        fixedBook.setAuthor(getMockAuthor1());

        when(mockBookRepository.findById(MOCK_BOOK_ID_1)).thenReturn(Optional.of(savedBookWithTypo));
        when(mockBookRepository.save(argThat(b -> b.getId() == MOCK_BOOK_ID_1))).thenReturn(fixedBook);

        Book actualBook = bookService.createOrUpdate(fixedBook);
        assertThat(actualBook)
                .extracting(PROPERTIES_TO_EXTRACT)
                .containsExactly(MOCK_TITLE_AOW, MOCK_AUTHOR_ST);

        verify(mockBookRepository, atMostOnce()).findById(anyLong());
        verify(mockBookRepository, atMostOnce()).save(any());
    }

    @Test
    @DisplayName("Get All - should get a list of books")
    void test_getAll() {
        when(mockBookRepository.findAll()).thenReturn(Arrays.asList(getMockBook1(), getMockBook2()));

        assertThat(bookService.getAll())
                .extracting(Book::getTitle)
                .containsExactly(MOCK_TITLE_AOW, MOCK_TITLE_TP);

        verify(mockBookRepository, atMostOnce()).findAll();
    }

    @Test
    @DisplayName("Get By ID - should get a list of books")
    void test_getById() {
        when(mockBookRepository.findById(1L)).thenReturn(Optional.of(getMockBook1()));

        assertThat(bookService.getById(1L))
                .extracting(Book::getTitle)
                .isEqualTo(MOCK_TITLE_AOW);

        verify(mockBookRepository, atMostOnce()).findById(anyLong());
    }

    @Test
    @DisplayName("Get By ID - should get a list of books")
    void test_getById_notFound() {
        when(mockBookRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RecordNotFoundException.class, () -> bookService.getById(1L));

        verify(mockBookRepository, atMostOnce()).findById(anyLong());
    }

    @Test
    @DisplayName("Get All (paged) - should get a page of books")
    void test_getAll_paged() {
        when(mockBookRepository.findAll(mockPageable)).thenReturn(mockBookPage);

        assertSame(mockBookPage, bookService.getAll(mockPageable));

        verify(mockBookRepository, atMostOnce()).findAll();
    }

    @Test
    @DisplayName("Get All - should get a page of books by the author ID")
    void test_getAllByAuthors_paged() {
        when(mockBookRepository.findByAuthorId(1L, mockPageable)).thenReturn(mockBookPage);

        assertSame(mockBookPage, bookService.getAllByAuthor(1L, mockPageable));

        verify(mockBookRepository, atMostOnce()).findAll();
    }

    @Test
    @DisplayName("Get Contains - should return books with title 'The'")
    void test_getContains() {
        List<Book> mockPageContent = List.of(getMockBook1());
        when(mockBookRepository.findByTitleContaining(MOCK_TITLE_CONTAINS, mockPageable)).thenReturn(new PageImpl<>(mockPageContent));
        Page<Book> actualBookPage = bookService.getContainingTitle(MOCK_TITLE_CONTAINS, mockPageable);
        assertEquals(mockPageContent, actualBookPage.getContent());

        verify(mockBookRepository, atMostOnce()).findByTitleContaining(any(), any());
    }

    @Test
    @DisplayName("Get Contains - should throw RecordNotFoundException when repository returns empty")
    void test_getContains_throwExceptionWhenEmpty() {
        when(mockBookRepository.findByTitleContaining(MOCK_TITLE_CONTAINS, mockPageable)).thenReturn(new PageImpl<>(Collections.emptyList()));
        assertThrows(RecordNotFoundException.class, () -> bookService.getContainingTitle(MOCK_TITLE_CONTAINS, mockPageable));

        verify(mockBookRepository, atMostOnce()).findByTitleContaining(any(), any());
    }

}
