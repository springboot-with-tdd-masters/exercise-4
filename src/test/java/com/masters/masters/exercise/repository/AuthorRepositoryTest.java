package com.masters.masters.exercise.repository;

import com.masters.masters.exercise.model.Author;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.Optional;

@DataJpaTest
public class AuthorRepositoryTest {

    @Autowired
    private AuthorRepository repo;

    @Test
    public void saveHappyPath(){
        Author entity = new Author();
        entity.setName("name");
        Author savedEntity = repo.save(entity);
        Assertions.assertEquals("name",savedEntity.getName());
        Assertions.assertNotNull(savedEntity.getId());
    }

    @Test
    public void findById(){
        Author entity = new Author();
        entity.setName("name");
        Author savedEntity = repo.save(entity);
        Optional<Author> existingAuthor = repo.findById(savedEntity.getId());
        Assertions.assertTrue(existingAuthor.isPresent());
        Assertions.assertEquals("name",existingAuthor.get().getName());
    }

    @Test
    public void findAllHappyPath(){
        Author entity1 = new Author();
        Author entity2 = new Author();
        entity1.setName("author1");
        entity2.setName("author2");
        Iterable<Author> entityList = repo.saveAll(Arrays.asList(entity1,entity2));
        Pageable pageable = PageRequest.of(0,5, Sort.by("name").ascending());
        org.assertj.core.api.Assertions.assertThat(repo.findAll(pageable)).containsAll(entityList);
    }

}
