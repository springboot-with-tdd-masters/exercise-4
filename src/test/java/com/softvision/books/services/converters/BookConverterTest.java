package com.softvision.books.services.converters;

import com.softvision.books.repositories.entities.AuthorEntity;
import com.softvision.books.repositories.entities.BookEntity;
import com.softvision.books.services.converters.impl.BookConverterImpl;
import com.softvision.books.services.domain.Author;
import com.softvision.books.services.domain.Book;
import com.softvision.books.services.domain.Pagination;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookConverterTest {

    @Mock
    private AuthorConverter authorConverter;

    private BookConverter bookConverter;

    @BeforeEach
    void setUp() {
        bookConverter = new BookConverterImpl(authorConverter);
    }

    @Test
    @DisplayName("shouldAcceptBookEntityAndConvertToBook")
    void shouldAcceptBookEntityAndConvertToBook() {

        // Arrange
        final String expectedTitle = "Expected Title";
        final String expectedDescription = "Expected Description";
        final Long expectedId = 1L;
        final BookEntity bookEntity = new BookEntity(expectedTitle, expectedDescription);
        bookEntity.setId(expectedId);

        AuthorEntity authorEntity = new AuthorEntity();
        bookEntity.setAuthor(authorEntity);

        final Author expectedAuthor = new Author();

        when(authorConverter.convert(authorEntity))
                .thenReturn(expectedAuthor);

        // Act
        final Book convertedBook = bookConverter.convert(bookEntity);

        // Assert
        assertThat(convertedBook)
                .extracting("id", "title", "description", "author")
                .contains(expectedId, expectedTitle, expectedDescription, expectedAuthor);
    }

    @Test
    @DisplayName("shouldAcceptBookAndConvertToBookEntity")
    void shouldAcceptBookAndConvertToBookEntity() {

        // Arrange
        final String expectedTitle = "Expected Title";
        final String expectedDescription = "Expected Description";
        final Long expectedId = 1L;
        final Book book = new Book(expectedTitle, expectedDescription);
        book.setId(expectedId);

        Author author = new Author();
        book.setAuthor(author);

        final AuthorEntity expectedAuthorEntity = new AuthorEntity();

        when(authorConverter.convert(author))
                .thenReturn(expectedAuthorEntity);

        // Act
        final BookEntity convertedBookEntity = bookConverter.convert(book);

        // Assert
        assertThat(convertedBookEntity)
                .extracting("id", "title", "description", "author")
                .contains(expectedId, expectedTitle, expectedDescription, expectedAuthorEntity);
    }


    @Test
    @DisplayName("convert_shouldAcceptPageOfBookEntityAndConvertToPaginationOfBook")
    void convert_shouldAcceptPageOfBookEntityAndConvertToPaginationOfBook() {
        // Arrange
        Page<BookEntity> bookEntityPage = mock(Page.class);
        Stream<BookEntity> bookStream = Stream.of(new BookEntity("Title", "Description"));
        final Pageable pageable = Pageable.ofSize(1);

        when(bookEntityPage.get())
                .thenReturn(bookStream);
        when(bookEntityPage.getTotalPages())
                .thenReturn(1);
        when(bookEntityPage.getTotalElements())
                .thenReturn(1L);

        when(bookEntityPage.getPageable())
                .thenReturn(pageable);

//        when(authorConverter.convert(any(AuthorEntity.class)))
//                .thenReturn(new Author());

        // Act
        final Pagination<Book> paginatedBook = bookConverter.convert(bookEntityPage);

        // Assert
        assertThat(paginatedBook)
                .isNotNull();
        assertThat(paginatedBook.getContents())
                .hasSize(1)
                .filteredOn("description", "Description")
                .isNotEmpty();
        assertThat(paginatedBook.getPage())
                .extracting("page", "maxPage", "size", "total")
                .containsExactly(0, 1, 1, 1L);
    }
}