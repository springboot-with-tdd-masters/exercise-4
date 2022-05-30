package com.softvision.books.services;

import com.softvision.books.exeptions.NotFoundException;
import com.softvision.books.repositories.AuthorRepository;
import com.softvision.books.repositories.entities.AuthorEntity;
import com.softvision.books.services.converters.AuthorConverter;
import com.softvision.books.services.domain.Author;
import com.softvision.books.services.domain.PageBean;
import com.softvision.books.services.domain.Pagination;
import com.softvision.books.services.impl.AuthorServiceImpl;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthorServiceTest {

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private AuthorConverter authorConverter;

    private AuthorService authorService;

    @BeforeEach
    void setUp() {
        authorService = new AuthorServiceImpl(authorRepository, authorConverter);
    }

    @Test
    @DisplayName("findById_shouldAcceptIdAndReturnAuthorWithThatId")
    void findById_shouldAcceptIdAndReturnAuthorWithThatId() {
        // Arrange
        Long id = 1L;
        AuthorEntity authorFromRepository = new AuthorEntity();
        Author convertedAuthor = new Author();
        convertedAuthor.setId(id);

        when(authorRepository.findById(id))
                .thenReturn(Optional.ofNullable(authorFromRepository));
        when(authorConverter.convert(authorFromRepository))
                .thenReturn(convertedAuthor);

        // Act
        final Author author = authorService.findById(id);

        // Assert
        verify(authorRepository)
                .findById(id);
        verify(authorConverter)
                .convert(authorFromRepository);

        assertThat(author)
                .isNotNull()
                .extracting("id")
                .isEqualTo(id);
    }

    @Test
    @DisplayName("findById_shouldThrowAnErrorWhenNotFound")
    void findById_shouldThrowAnErrorWhenNotFound() {
        // Arrange

        // Act
        final Throwable throwable = catchThrowable(() -> authorService.findById(1L));

        // Assert
        assertThat(throwable)
                .hasMessage("Author not found")
                .asInstanceOf(InstanceOfAssertFactories.type(NotFoundException.class));
    }

    @Test
    @DisplayName("findAll_shouldReturnAllAuthorsFromTheRepository")
    void findAll_shouldReturnAllAuthorsFromTheRepository() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 2);
        Page<AuthorEntity> page = mock(Page.class);

        final List<Author> authors = Arrays.asList(new Author("James"), new Author("Rod"));
        final PageBean pageBean = new PageBean.Builder()
                .page(0)
                .maxPage(1)
                .size(2)
                .total(2L)
                .build();

        final Pagination<Author> expectedResult = Pagination.of(authors, pageBean);

        when(authorRepository.findAll(pageable))
                .thenReturn(page);
        when(authorConverter.convert(page))
                .thenReturn(expectedResult);

        // Act
        final Pagination<Author> authorPagination = authorService.findAll(pageable);

        // Assert
        verify(authorRepository)
                .findAll(pageable);
        verify(authorConverter)
                .convert(page);

        assertThat(authorPagination)
                .isEqualTo(expectedResult);
    }

    @Test
    @DisplayName("save_shouldAcceptAuthorSaveAuthorToRepository")
    void save_shouldSaveAuthorToRepository() {
        // Arrange
        final String authorName = "James";
        Author author = new Author(authorName);
        AuthorEntity authorEntity = new AuthorEntity(authorName);

        when(authorConverter.convert(author))
                .thenReturn(authorEntity);
        when(authorRepository.save(authorEntity))
                .thenReturn(authorEntity);
        when(authorConverter.convert(authorEntity))
                .thenReturn(author);

        // Act
        final Author savedAuthor = authorService.save(author);

        // Assert
        verify(authorConverter)
                .convert(author);
        verify(authorRepository)
                .save(authorEntity);
        verify(authorConverter)
                .convert(authorEntity);

        assertThat(savedAuthor)
                .isNotNull()
                .extracting("name")
                .isEqualTo(authorName);
    }

    @Test
    @DisplayName("update_shouldAcceptAuthorAndUpdateAuthorEntity")
    void update_shouldAcceptAuthorAndUpdateAuthorEntity() {
        // Arrange
        Long id = 1L;
        Author author = new Author("Name Update Request");
        AuthorEntity authorEntity = new AuthorEntity("James");
        Author updatedAuthor = new Author("Updated Name");

        when(authorRepository.findById(id))
                .thenReturn(Optional.ofNullable(authorEntity));
        when(authorConverter.convert(authorEntity))
                .thenReturn(updatedAuthor);

        // Act
        final Author actualAuthor = authorService.update(id, author);

        // Assert
        verify(authorRepository)
                .findById(id);
        verify(authorConverter)
                .convert(authorEntity);
        verify(authorRepository)
                .save(authorEntity);

        assertThat(actualAuthor)
                .isEqualTo(updatedAuthor);
    }

    @Test
    @DisplayName("update_shouldThrowNotFoundExceptionIfAuthorDoesntExists")
    void update_shouldThrowNotFoundExceptionIfAuthorDoesntExists() {
        // Arrange

        // Act
        final Throwable throwable = catchThrowable(() -> authorService.update(1L, new Author()));

        // Assert
        assertThat(throwable)
                .hasMessage("Author not found")
                .asInstanceOf(InstanceOfAssertFactories.type(NotFoundException.class));
    }

    @Test
    @DisplayName("delete_shouldDeleteAuthor")
    void delete_shouldDeleteAuthor() {
        // Arrange
        Long id = 1L;
        AuthorEntity authorEntity = new AuthorEntity();

        when(authorRepository.findById(id))
                .thenReturn(Optional.ofNullable(authorEntity));

        // Act
        authorService.deleteById(id);

        // Assert
        verify(authorRepository)
                .delete(authorEntity);
    }

    @Test
    @DisplayName("delete_shouldThrowNotFoundExceptionIfAuthorDoesntExists")
    void delete_shouldThrowNotFoundExceptionIfAuthorDoesntExists() {
        // Arrange

        // Act
        final Throwable throwable = catchThrowable(() -> authorService.deleteById(1L));

        // Assert
        assertThat(throwable)
                .hasMessage("Author not found")
                .asInstanceOf(InstanceOfAssertFactories.type(NotFoundException.class));
    }
}
