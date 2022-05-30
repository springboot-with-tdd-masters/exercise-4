package com.softvision.books.services.converters;

import com.softvision.books.repositories.entities.AuthorEntity;
import com.softvision.books.services.converters.impl.AuthorConverterImpl;
import com.softvision.books.services.domain.Author;
import com.softvision.books.services.domain.Pagination;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuthorConverterTest {

    private final AuthorConverter authorConverter = new AuthorConverterImpl();

    @Test
    @DisplayName("convert_shouldAcceptAuthorAndConvertToAuthorEntity")
    void convert_shouldAcceptAuthorAndConvertToAuthorEntity() {
        // Arrange
        Author author = new Author("James");
        author.setId(1L);

        // Act
        final AuthorEntity convertedAuthorEntity = authorConverter.convert(author);

        // Assert
        assertThat(convertedAuthorEntity)
                .extracting("id", "name")
                .containsExactly(1L, "James");
    }

    @Test
    @DisplayName("convert_shouldAcceptAuthorEntityAndConvertToAuthor")
    void convert_shouldAcceptAuthorEntityAndConvertToAuthor() {
        // Arrange
        AuthorEntity authorEntity = new AuthorEntity("James");
        authorEntity.setId(1L);

        // Act
        final Author convertedAuthor = authorConverter.convert(authorEntity);

        // Assert
        assertThat(convertedAuthor)
                .extracting("id", "name")
                .containsExactly(1L, "James");
    }

    @Test
    @DisplayName("convert_shouldAcceptPageOfAuthorEntityAndConvertToPaginationOfAuthor")
    void convert_shouldAcceptPageOfAuthorEntityAndConvertToPaginationOfAuthor() {
        // Arrange
        Page<AuthorEntity> authorEntityPage = mock(Page.class);
        Stream<AuthorEntity> authorStream = Stream.of(new AuthorEntity("James"));
        final Pageable pageable = Pageable.ofSize(1);

        when(authorEntityPage.get())
                .thenReturn(authorStream);
        when(authorEntityPage.getTotalPages())
                .thenReturn(1);
        when(authorEntityPage.getTotalElements())
                .thenReturn(1L);

        when(authorEntityPage.getPageable())
                .thenReturn(pageable);

        // Act
        final Pagination<Author> paginatedAuthor = authorConverter.convert(authorEntityPage);

        // Assert
        assertThat(paginatedAuthor)
                .isNotNull();
        assertThat(paginatedAuthor.getContents())
                .hasSize(1)
                .filteredOn("name", "James")
                .isNotEmpty();
        assertThat(paginatedAuthor.getPage())
                .extracting("page", "maxPage", "size", "total")
                .containsExactly(0, 1, 1, 1L);
    }
}