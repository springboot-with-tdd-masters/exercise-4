package com.softvision.library.tdd.repository;

import com.softvision.library.tdd.model.Author;
import com.softvision.library.tdd.model.Book;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static com.softvision.library.tdd.LibraryMocks.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@DataJpaTest
public class BookRepositoryTests {

    @Autowired
    BookRepository bookRepository;

    @Autowired
    AuthorRepository authorRepository;

    static final String[] PROPERTIES_TO_EXTRACT = {"title", "author.name"};

    @Test
    @DisplayName("Save - should accept different books and save the correct respective details")
    void test_save() {
        Author savedAuthor1 = authorRepository.save(getMockAuthor1());
        Book savedBook1 = getMockBook1();
        savedBook1.setAuthor(savedAuthor1);
        savedBook1 = bookRepository.save(savedBook1);
        assertThat(savedBook1)
                .extracting(PROPERTIES_TO_EXTRACT)
                .containsExactly(MOCK_TITLE_AOW, MOCK_AUTHOR_ST);

        Author savedAuthor2 = authorRepository.save(getMockAuthor2());
        Book savedBook2 = getMockBook2();
        savedBook2.setAuthor(savedAuthor2);
        savedBook2 = bookRepository.save(savedBook2);
        assertThat(savedBook2)
                .extracting(PROPERTIES_TO_EXTRACT)
                .containsExactly(MOCK_TITLE_TP, MOCK_AUTHOR_NM);

    }

    @Test
    @DisplayName("Find By Title Containing - should return the respective list of books that contains substrings 'Prince' and 'The'")
    void test_findByTitleContaining() {
        Author savedAuthor1 = authorRepository.save(getMockAuthor1());
        Book savedBook1 = getMockBook1();
        savedBook1.setAuthor(savedAuthor1);
        bookRepository.save(savedBook1);

        Author savedAuthor2 = authorRepository.save(getMockAuthor2());
        Book savedBook2 = getMockBook2();
        savedBook2.setAuthor(savedAuthor2);
        bookRepository.save(savedBook2);

        assertThat(bookRepository.findByTitleContaining("Prince", Pageable.unpaged()))
                .extracting(Book::getTitle)
                .contains(MOCK_TITLE_TP)
                .doesNotContain(MOCK_TITLE_AOW);
        assertThat(bookRepository.findByTitleContaining("The", Pageable.unpaged()))
                .extracting(Book::getTitle)
                .containsExactly(MOCK_TITLE_AOW, MOCK_TITLE_TP);
        assertThat(bookRepository.findByTitleContaining("The", PageRequest.of(0, 1)))
                .extracting(Book::getTitle)
                .contains(MOCK_TITLE_AOW)
                .doesNotContain(MOCK_TITLE_TP);
    }

    @Test
    @DisplayName("Find All - should be able to retrieve all saved books")
    void test_findAll() {
        Author savedAuthor1 = authorRepository.save(getMockAuthor1());
        Book book1 = getMockBook1();
        book1.setAuthor(savedAuthor1);
        bookRepository.save(book1);

        Author savedAuthor2 = authorRepository.save(getMockAuthor2());
        Book book2 = getMockBook2();
        book2.setAuthor(savedAuthor2);
        bookRepository.save(book2);

        assertThat(bookRepository.findAll())
                .extracting(PROPERTIES_TO_EXTRACT)
                .contains(tuple(MOCK_TITLE_AOW, MOCK_AUTHOR_ST), tuple(MOCK_TITLE_TP, MOCK_AUTHOR_NM));
    }

    @Test
    @DisplayName("Find By ID - should be able to retrieve a book given the ID")
    void test_findById() {
        Author savedAuthor1 = authorRepository.save(getMockAuthor1());
        Book book1 = getMockBook1();
        book1.setAuthor(savedAuthor1);
        bookRepository.save(book1);

        assertThat(bookRepository.findById(book1.getId()))
                .map(Book::getTitle)
                .get().isEqualTo(MOCK_TITLE_AOW);
    }

    @Test
    @DisplayName("Find By Author ID - should be able to retrieve all books written by the author")
    void test_findByAuthorId() {
        Author savedAuthor1 = authorRepository.save(getMockAuthor1());
        Book book1 = getMockBook1();
        book1.setAuthor(savedAuthor1);
        bookRepository.save(book1);

        assertThat(bookRepository.findByAuthorId(savedAuthor1.getId(), null))
                .extracting(Book::getTitle)
                .contains(MOCK_TITLE_AOW);
    }

    @AfterEach
    void cleanup() {
        bookRepository.deleteAll();
        assertThat(bookRepository.findAll()).isEmpty();
    }
}
