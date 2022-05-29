package com.softvision.books.services;

import com.softvision.books.exeptions.NotFoundException;
import com.softvision.books.repositories.AuthorRepository;
import com.softvision.books.repositories.BookRepository;
import com.softvision.books.repositories.entities.AuthorEntity;
import com.softvision.books.repositories.entities.BookEntity;
import com.softvision.books.services.converters.BookConverter;
import com.softvision.books.services.domain.*;
import com.softvision.books.services.impl.BookServiceImpl;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
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
public class BookServiceTest {

    private BookService bookService;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookConverter bookConverter;

    @Mock
    private AuthorRepository authorRepository;

    @BeforeEach
    void setUp() {
        bookService = new BookServiceImpl(bookRepository, bookConverter, authorRepository);
    }

    @Test
    @DisplayName("findAll_shouldReturnAllBookEntities")
    void findAll_shouldReturnAllBookEntities() {
        // Arrange
        Pageable pageRequest = Pageable.unpaged();
        Page<BookEntity> page = mock(Page.class);

        final List<Book> books = Arrays.asList(new Book("Some Title", "Description 1"));
        final PageBean pageBean = new PageBean.Builder()
                .page(0)
                .maxPage(1)
                .size(2)
                .total(2L)
                .build();

        final Pagination<Book> expectedResult = Pagination.of(books, pageBean);

        when(bookRepository.findAll(pageRequest))
                .thenReturn(page);
        when(bookConverter.convert(page))
                .thenReturn(expectedResult);

        // Act
        final Pagination<Book> bookPagination = bookService.findAll(BookFilter.empty(), pageRequest);

        // Assert
        verify(bookRepository)
                .findAll(pageRequest);
        verify(bookConverter)
                .convert(page);

        assertThat(bookPagination)
                .isNotNull();
        assertThat(bookPagination)
                .isEqualTo(expectedResult);
    }

    @Test
    @DisplayName("findAll_shouldReturnAllBookEntitiesWithTitleContainingTheSearchKey")
    void findAll_shouldReturnAllBookEntitiesWithTitleContainingTheSearchKey() {
        // Arrange
        Pageable pageRequest = Pageable.unpaged();
        String searchKey = "Some Title";
        Page<BookEntity> page = mock(Page.class);

        final List<Book> books = Arrays.asList(new Book(searchKey, "Description 1"));
        final PageBean pageBean = new PageBean.Builder()
                .page(0)
                .maxPage(1)
                .size(2)
                .total(2L)
                .build();

        final Pagination<Book> expectedResult = Pagination.of(books, pageBean);

        when(bookRepository.findByTitleContainingIgnoreCase(searchKey.toLowerCase(), pageRequest))
                .thenReturn(page);
        when(bookConverter.convert(page))
                .thenReturn(expectedResult);

        // Act
        final Pagination<Book> bookPagination = bookService.findAll(BookFilter.of(searchKey), pageRequest);

        // Assert
        verify(bookRepository)
                .findByTitleContainingIgnoreCase(searchKey.toLowerCase(), pageRequest);
        verify(bookConverter)
                .convert(page);

        assertThat(bookPagination)
                .isNotNull();
        assertThat(bookPagination)
                .isEqualTo(expectedResult);
    }

    @Test
    @DisplayName("findAll_shouldReturnAllBookEntitiesAsBooksFromRepository")
    void findAll_shouldReturnAllBookEntitiesAsBooksFromRepository() {

        // Arrange
        Pageable pageable = PageRequest.of(0, 2);
        Page<BookEntity> page = mock(Page.class);

        final List<Book> books = Arrays.asList(new Book("Title 1", "Description 1"));
        final PageBean pageBean = new PageBean.Builder()
                .page(0)
                .maxPage(1)
                .size(2)
                .total(2L)
                .build();

        final Pagination<Book> expectedResult = Pagination.of(books, pageBean);

        when(bookRepository.findByAuthorId(1L, pageable))
                .thenReturn(page);
        when(bookConverter.convert(page))
                .thenReturn(expectedResult);

        // Act
        final Pagination<Book> bookPagination = bookService.findAll(BookFilter.of(1L), pageable);

        // Assert
        verify(bookRepository)
                .findByAuthorId(1L, pageable);
        verify(bookConverter)
                .convert(page);

        assertThat(bookPagination)
                .isEqualTo(expectedResult);
    }

    @Test
    @DisplayName("findById_shouldAcceptIdAndReturnBookWithThatId")
    void findById_shouldAcceptIdAndReturnBookWithThatId() {
        // Arrange
        Long id = 1L;
        BookEntity bookFromRepository = new BookEntity();
        Book convertedBook = new Book();
        convertedBook.setId(id);

        when(bookRepository.findById(id))
                .thenReturn(Optional.ofNullable(bookFromRepository));
        when(bookConverter.convert(bookFromRepository))
                .thenReturn(convertedBook);

        // Act
        final Book book = bookService.findById(id);

        // Assert
        verify(bookRepository)
                .findById(id);
        verify(bookConverter)
                .convert(bookFromRepository);

        assertThat(book)
                .isNotNull()
                .extracting("id")
                .isEqualTo(id);
    }

    @Test
    @DisplayName("findById_shouldThrowAnErrorWhenNotFound")
    void findById_shouldThrowAnErrorWhenNotFound() {
        // Arrange

        // Act
        final Throwable throwable = catchThrowable(() -> bookService.findById(1L));

        // Assert
        assertThat(throwable)
                .hasMessage("Book not found")
                .asInstanceOf(InstanceOfAssertFactories.type(NotFoundException.class));
    }

    @Test
    @DisplayName("save_shouldAcceptBookAndSaveToRepository")
    void save_shouldAcceptBookAndSaveToRepository() {
        // Arrange
        Long authorId = 1L;
        final String title = "Head first";
        final String description = "Happy book";

        AuthorEntity authorEntity = new AuthorEntity();
        Book bookRequest = new Book(title, description);
        BookEntity bookEntity = new BookEntity(title, description);
        Author author = new Author("Andrew Stellman");
        Book convertedBook = new Book(1L, title, description, author);

        when(authorRepository.findById(authorId))
                .thenReturn(Optional.ofNullable(authorEntity));
        when(bookConverter.convert(bookRequest))
                .thenReturn(bookEntity);
        when(bookRepository.save(bookEntity))
                .thenReturn(bookEntity);
        when(bookConverter.convert(bookEntity))
                .thenReturn(convertedBook);

        // Act
        final Book actualBook = bookService.add(authorId, bookRequest);

        // Assert
        verify(bookRepository)
                .save(bookEntity);

        assertThat(actualBook)
                .extracting("title", "description", "author")
                .containsExactly(title, description, author);
    }

    @Test
    @DisplayName("save_shouldThrowNotFoundExceptionWhenAuthorIdDoesntNotExists")
    void save_shouldThrowNotFoundExceptionWhenAuthorIdDoesntNotExists() {
        // Arrange
        // Act
        final Throwable throwable = catchThrowable(() -> bookService.add(1L, new Book()));

        // Assert
        assertThat(throwable)
                .hasMessage("Author not found, book must have an Author")
                .asInstanceOf(InstanceOfAssertFactories.type(NotFoundException.class));
    }

    @Test
    @DisplayName("update_shouldAcceptBookAndUpdateBookEntity")
    void update_shouldAcceptBookAndUpdateBookEntity() {
        // Arrange
        Long id = 1L;
        Book bookRequest = new Book("Update Title Request", "Update Description Request");
        BookEntity bookEntity = new BookEntity("Some book", "Some book description");
        Book updatedBook = new Book("Update Title Request", "Update Description Request");

        when(bookRepository.findById(id))
                .thenReturn(Optional.ofNullable(bookEntity));
        when(bookConverter.convert(bookEntity))
                .thenReturn(updatedBook);

        // Act
        final Book actualBook = bookService.update(id, bookRequest);

        // Assert
        verify(bookRepository)
                .findById(id);
        verify(bookConverter)
                .convert(bookEntity);
        verify(bookRepository)
                .save(bookEntity);

        assertThat(actualBook)
                .isEqualTo(updatedBook);
    }

    @Test
    @DisplayName("update_shouldThrowNotFoundExceptionIfBookDoesntExists")
    void update_shouldThrowNotFoundExceptionIfBookDoesntExists() {
        // Arrange

        // Act
        final Throwable throwable = catchThrowable(() -> bookService.update(1L, new Book()));

        // Assert
        assertThat(throwable)
                .hasMessage("Book not found")
                .asInstanceOf(InstanceOfAssertFactories.type(NotFoundException.class));
    }

    @Test
    @DisplayName("delete_shouldDeleteBook")
    void delete_shouldDeleteBook() {
        // Arrange
        Long id = 1L;
        BookEntity bookEntity = new BookEntity();

        when(bookRepository.findById(id))
                .thenReturn(Optional.ofNullable(bookEntity));

        // Act
        bookService.deleteById(id);

        // Assert
        verify(bookRepository)
                .delete(bookEntity);
    }

    @Test
    @DisplayName("delete_shouldThrowNotFoundExceptionIfBookDoesntExists")
    void delete_shouldThrowNotFoundExceptionIfBookDoesntExists() {
        // Arrange

        // Act
        final Throwable throwable = catchThrowable(() -> bookService.deleteById(1L));

        // Assert
        assertThat(throwable)
                .hasMessage("Book not found")
                .asInstanceOf(InstanceOfAssertFactories.type(NotFoundException.class));
    }
}
