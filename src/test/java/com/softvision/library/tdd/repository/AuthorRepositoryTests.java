package com.softvision.library.tdd.repository;

import com.softvision.library.tdd.model.Author;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;

import static com.softvision.library.tdd.LibraryMocks.*;
import static com.softvision.library.tdd.LibraryMocks.MOCK_AUTHOR_NM;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class AuthorRepositoryTests {

    @Autowired
    AuthorRepository authorRepository;

    @Mock
    Pageable pageable;

    static final String[] PROPERTIES_TO_EXTRACT = {"name"};

    @Test
    @DisplayName("Save - should accept different authors and save the correct respective details")
    void test_save() {
        Author savedAuthor1 = authorRepository.save(getMockAuthor1());
        assertThat(savedAuthor1)
                .extracting(Author::getName)
                .isEqualTo(MOCK_AUTHOR_ST);

        Author savedAuthor2 = authorRepository.save(getMockAuthor2());
        assertThat(savedAuthor2)
                .extracting(Author::getName)
                .isEqualTo(MOCK_AUTHOR_NM);
    }

    @Test
    @DisplayName("Find All - should be able to retrieve all saved authors")
    void test_findAll() {
        authorRepository.save(getMockAuthor1());
        authorRepository.save(getMockAuthor2());

        assertThat(authorRepository.findAll())
                .extracting(Author::getName)
                .contains(MOCK_AUTHOR_ST, MOCK_AUTHOR_NM);
    }

    @Test
    @DisplayName("Find By ID - should be able to retrieve an author given the ID")
    void test_findById() {
        Author savedAuthor1 = authorRepository.save(getMockAuthor1());

        assertThat(authorRepository.findById(savedAuthor1.getId()))
                .map(Author::getName)
                .get().isEqualTo(MOCK_AUTHOR_ST);
    }

    @Test
    @DisplayName("Find By Name Containing - should be able to retrieve authors with the substring 'Sun'")
    void test_findByNameContaining() {
        authorRepository.save(getMockAuthor1());
        authorRepository.save(getMockAuthor2());

        assertThat(authorRepository.findByNameContaining("Sun", Pageable.unpaged()))
                .extracting(Author::getName)
                .contains(MOCK_AUTHOR_ST)
                .doesNotContain(MOCK_AUTHOR_NM);

    }


}
