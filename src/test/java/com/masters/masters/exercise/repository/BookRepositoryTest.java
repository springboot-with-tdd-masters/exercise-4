package com.masters.masters.exercise.repository;

import com.masters.masters.exercise.model.Author;
import com.masters.masters.exercise.model.BookEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@DataJpaTest
public class BookRepositoryTest {

    @Autowired
    BookRepository repository;

    @Autowired
    AuthorRepository authorRepository;

    @Test
    public void saveHappyPath(){
        BookEntity entity = new BookEntity();
        entity.setTitle("title");
        entity.setDescription("description");
        entity.setAuthor(new Author());
        BookEntity savedEntity = repository.save(entity);
        org.junit.jupiter.api.Assertions.assertEquals("title",savedEntity.getTitle());
    }

    @Test
    public void findAllHappyPath(){
        BookEntity entity1 = new BookEntity();
        BookEntity entity2 = new BookEntity();
        entity1.setTitle("title1");
        entity2.setTitle("title2");
        Iterable<BookEntity> entityList = repository.saveAll(List.of(entity1,entity2));
        Assertions.assertThat(repository.findAll()).containsAll(entityList);
    }

    @Test
    public void findAllNoRecords(){
        Assertions.assertThat(repository.findAll()).isEmpty();
    }

    @Test
    public void findbyId(){
        BookEntity entity = new BookEntity();
        entity.setDescription("description");
        entity.setTitle("title");
        entity.setAuthor(new Author());
        BookEntity savedEntity = repository.save(entity);
        Optional<BookEntity> existingBook = repository.findById(savedEntity.getId());
        org.junit.jupiter.api.Assertions.assertTrue(existingBook.isPresent());
        org.junit.jupiter.api.Assertions.assertEquals("title",existingBook.get().getTitle());
    }

    @Test
    public void findByTitleContaining(){
        BookEntity entity = new BookEntity();
        Author author = new Author();
        author.setId(Long.parseLong("1"));
        author.setName("name");
        Author savedAuthor = authorRepository.save(author);
        entity.setDescription("description");
        entity.setTitle("title");
        entity.setAuthor(savedAuthor);
        BookEntity entity2 = new BookEntity();
        entity2.setDescription("description2");
        entity2.setTitle("title2");
        entity2.setAuthor(savedAuthor);;
        Pageable pageable = PageRequest.of(0,20);
        repository.saveAll(List.of(entity,entity2));
        List<BookEntity> existingBook = repository.findByTitleContaining("ti",pageable);
        org.junit.jupiter.api.Assertions.assertEquals(2,existingBook.size());
    }

    @Test
    public void findByAuthor(){
        BookEntity entity = new BookEntity();
        Author author = new Author();
        author.setId(Long.parseLong("1"));
        author.setName("name");
        Author savedAuthor = authorRepository.save(author);
        entity.setDescription("description");
        entity.setTitle("title");
        entity.setAuthor(savedAuthor);
        BookEntity entity2 = new BookEntity();
        entity2.setDescription("description2");
        entity2.setTitle("title2");
        entity2.setAuthor(savedAuthor);
        Pageable pageable = PageRequest.of(0,20);
        repository.saveAll(List.of(entity,entity2));
        List<BookEntity> existingBook = repository.findByAuthor(savedAuthor,pageable);
        org.junit.jupiter.api.Assertions.assertEquals(2,existingBook.size());
    }

    @Test
    public void findByAuthorAndId(){
        BookEntity entity = new BookEntity();
        Author author = new Author();
        author.setId(Long.parseLong("1"));
        author.setName("name");
        Author savedAuthor = authorRepository.save(author);
        entity.setId(Long.parseLong("1"));
        entity.setDescription("description");
        entity.setTitle("title");
        entity.setAuthor(savedAuthor);
        BookEntity entity2 = new BookEntity();
        entity2.setDescription("description2");
        entity2.setTitle("title2");
        entity2.setAuthor(savedAuthor);
        entity.setId(Long.parseLong("2"));
        Pageable pageable = PageRequest.of(0,20);

        repository.saveAll(List.of(entity,entity2));
        List<BookEntity> existingBook = repository.findByAuthorAndId(savedAuthor,Long.parseLong("1"),pageable);
        org.junit.jupiter.api.Assertions.assertEquals(0,existingBook.size());
    }

}
