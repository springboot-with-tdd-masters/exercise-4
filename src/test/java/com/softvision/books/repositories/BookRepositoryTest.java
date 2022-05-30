package com.softvision.books.repositories;

import com.softvision.books.repositories.entities.AuthorEntity;
import com.softvision.books.repositories.entities.BookEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    TestEntityManager testEntityManager;

    @Test
    @DisplayName("findByAuthorId_shouldAcceptAuthorIdAndPageRequestAndReturnAllBooksWithThatAuthorId")
    void findByAuthorId_shouldAcceptAuthorIdAndPageRequestAndReturnAllBooksWithThatAuthorId() {
        // Arrange
        final AuthorEntity author = new AuthorEntity("James");
        testEntityManager.persist(author);

        final BookEntity firstBook = new BookEntity("Title 1", "Description 1");
        firstBook.setAuthor(author);
        testEntityManager.persist(firstBook);

        final BookEntity secondBook = new BookEntity("Title 2", "Description 2");
        secondBook.setAuthor(author);
        testEntityManager.persist(secondBook);

        final PageRequest pageRequest = PageRequest.of(0, 2);

        // Act
        final Page<BookEntity> bookEntityPage = bookRepository.findByAuthorId(author.getId(), pageRequest);

        // Assert
        assertThat(bookEntityPage.getTotalElements())
                .isEqualTo(2);
        assertThat(bookEntityPage.getSize())
                .isEqualTo(2);

        final Pageable pageable = bookEntityPage.getPageable();

        assertThat(pageable.getPageNumber())
                .isEqualTo(0);
        assertThat(pageable.getPageSize())
                .isEqualTo(2);
    }

    @Test
    @DisplayName("findByAuthorId_shouldAcceptAuthorIdAndPageRequestAndReturnAllBooksWithThatAuthorId")
    void findByAuthorId_shouldAcceptAuthorIdAndPageRequestAndReturnAllBooksWithThatAuthorId_casePageSizeOneAndPageNumberTwo() {
        // Arrange
        final AuthorEntity author = new AuthorEntity("James");
        testEntityManager.persist(author);

        final BookEntity firstBook = new BookEntity("Title 1", "Description 1");
        firstBook.setAuthor(author);
        testEntityManager.persist(firstBook);

        final BookEntity secondBook = new BookEntity("Title 2", "Description 2");
        secondBook.setAuthor(author);
        testEntityManager.persist(secondBook);

        final PageRequest pageRequest = PageRequest.of(1, 1);

        // Act
        final Page<BookEntity> bookEntityPage = bookRepository.findByAuthorId(author.getId(), pageRequest);

        // Assert
        assertThat(bookEntityPage.getTotalElements())
                .isEqualTo(2);
        assertThat(bookEntityPage.getSize())
                .isEqualTo(1);
        assertThat(bookEntityPage.get().collect(Collectors.toList()))
                .filteredOn("title", "Title 2")
                .isNotEmpty();

        final Pageable pageable = bookEntityPage.getPageable();

        assertThat(pageable.getPageNumber())
                .isEqualTo(1);
        assertThat(pageable.getPageSize())
                .isEqualTo(1);
    }

    @Test
    @DisplayName("findByTitleContaining_shouldAcceptTitleStringAndPageRequestAndReturnAllBooksContainingThatSearchKey")
    void findByTitleContaining_shouldAcceptTitleStringAndPageRequestAndReturnAllBooksContainingThatSearchKey() {
        // Arrange
        final AuthorEntity author = new AuthorEntity("James");
        testEntityManager.persist(author);

        final BookEntity firstBook = new BookEntity("Head First 1", "Description 1");
        firstBook.setAuthor(author);
        testEntityManager.persist(firstBook);

        final BookEntity secondBook = new BookEntity("Head First 2", "Description 2");
        secondBook.setAuthor(author);
        testEntityManager.persist(secondBook);

        final BookEntity thirdBook = new BookEntity("Head Second 3", "Description 2");
        thirdBook.setAuthor(author);
        testEntityManager.persist(thirdBook);

        final PageRequest pageRequest = PageRequest.of(0, 3);

        // Act
        final Page<BookEntity> bookEntityPage = bookRepository.findByTitleContainingIgnoreCase("ead first", pageRequest);

        // Assert
        assertThat(bookEntityPage.getTotalElements())
                .isEqualTo(2);
        assertThat(bookEntityPage.getSize())
                .isEqualTo(3);

        final List<BookEntity> bookEntities = bookEntityPage.get().collect(Collectors.toList());

        assertThat(bookEntities)
                .filteredOn("title", "Head First 1")
                .isNotEmpty();
        assertThat(bookEntities)
                .filteredOn("title", "Head First 2")
                .isNotEmpty();
        assertThat(bookEntities)
                .filteredOn("title", "Head Second 3")
                .isEmpty();

        final Pageable pageable = bookEntityPage.getPageable();

        assertThat(pageable.getPageNumber())
                .isEqualTo(0);
        assertThat(pageable.getPageSize())
                .isEqualTo(3);
    }
}
