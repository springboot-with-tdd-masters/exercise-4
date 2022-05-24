package com.magenic.mobog.exercise3app.repositories;

import com.magenic.mobog.exercise3app.entities.Author;
import com.magenic.mobog.exercise3app.entities.Book;
import com.magenic.mobog.exercise3app.entities.audit.AuditingConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {AuditingConfig.class}))
public class AuthorRepositoryTest {

    @Autowired
    TestEntityManager testEntityMgr;

    @Autowired
    AuthorRepository authorRepository;

    @AfterEach
    void teardown(){
        testEntityMgr.clear();
        testEntityMgr.flush();
    }
    @Test
    @DisplayName("findAll should retrieve all author")
    void findAllShouldRetrieveAllAuthors() {
        // given
        Author author1 = new Author();
        Author author2 = new Author();
        // when
        testEntityMgr.persist(author1);
        testEntityMgr.persist(author2);
        List<Author> actual = authorRepository.findAll();

        // then
        assertNotNull(actual);
        assertEquals(List.of(author1, author2), actual);

    }
    @Test
    @DisplayName("findAll should retrieve all author with books")
    void findAllShouldRetrieveAuthorWithBooks() {
        // given
        Author author1 = new Author();
        author1.setName("Finn Jake");


        Book book1 = new Book();
        book1.setAuthor(author1);
        book1.setTitle("Exploration Moment");

        author1.setBooks(List.of(book1));

        testEntityMgr.persist(author1);
        // when
        Optional<Author> saved = authorRepository.findById(author1.getId());
        Author actual = saved.get();
        // then
        assertNotNull(actual);
        List<Book> actualBooks = actual.getBooks();
        assertEquals(List.of(book1), actualBooks);
    }
    @Test
    @DisplayName("save should save author entity")
    void saveShouldSaveAuthorEntity() {
        // given
        Author newAuthor = new Author();
        newAuthor.setId(1L);
        newAuthor.setName("Robert Pattinson");
        // when
        Author expected = authorRepository.save(newAuthor);
        Author actual = testEntityMgr.find(Author.class, expected.getId());
        // then
        assertNotNull(actual);
        assertEquals(actual.getName(), expected.getName());
        assertEquals(actual.getId(), expected.getId());
    }
    @Test
    @DisplayName("save should save author entity with Audit")
    void saveShouldSaveAuthorEntityWithAudit() {
        // given
        Author newAuthor = new Author();
        newAuthor.setId(1L);
        newAuthor.setName("Robert Pattinson");
        // when
        Author expected = authorRepository.save(newAuthor);
        Author actual = testEntityMgr.find(Author.class, expected.getId());
        // then
        assertNotNull(actual);
        assertEquals(actual.getName(), expected.getName());
        assertEquals(actual.getId(), expected.getId());
        // assert audit
        assertNotNull(actual.getCreatedDate());
        assertNotNull(actual.getLastModifiedDate());
        assertEquals(actual.getCreatedBy(), "mobog");
    }
    @Test
    @DisplayName("should return emptyList when repository is empty")
    void shouldReturnEmptyList() {
        // given
        // when
        List<Author> actual = authorRepository.findAll();

        // then
        assertEquals(0, actual.size());
    }
    @Test
    @DisplayName("should return page with page 0 and size 2 ")
    void shouldReturnPageOfBookEntityPage0(){
        Author author = new Author();
        author.setName("A. Ang");
        Author author2 = new Author();
        author.setName("K. Tara");
        Author author3 = new Author();
        author.setName("Toph Beifong");
        Author author4 = new Author();
        author.setName("Z. Uko");
        testEntityMgr.persist(author);
        testEntityMgr.persist(author2);
        testEntityMgr.persist(author3);
        testEntityMgr.persist(author4);


        Page<Author> page = authorRepository.findAll(PageRequest.of(0, 2));
        assertEquals(List.of(author, author2), page.toList());
    }
    @Test
    @DisplayName("should return page with page 1 and size 2 ")
    void shouldReturnPageOfBookEntityPage1(){
        Author author = new Author();
        author.setName("A. Ang");
        Author author2 = new Author();
        author.setName("K. Tara");
        Author author3 = new Author();
        author.setName("Toph Beifong");
        Author author4 = new Author();
        author.setName("Z. Uko");
        testEntityMgr.persist(author);
        testEntityMgr.persist(author2);
        testEntityMgr.persist(author3);
        testEntityMgr.persist(author4);

        Page<Author> page = authorRepository.findAll(PageRequest.of(1, 2));
        assertEquals(List.of(author3, author4), page.toList());
    }
}
