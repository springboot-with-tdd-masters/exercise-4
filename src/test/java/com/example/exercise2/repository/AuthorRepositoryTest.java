package com.example.exercise2.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.exercise2.repository.entity.AuthorEntity;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@DataJpaTest
public class AuthorRepositoryTest {

  @Autowired
  private AuthorRepository authorRepository;


  @Test
  @DisplayName("Save - should accept entity and save details")
  void saveAuthor() {

    AuthorEntity expectedAuthor = new AuthorEntity(1L, "QWERTY");

    AuthorEntity savedAuthor = authorRepository.save(expectedAuthor);

    //verify
    assertThat(savedAuthor.getName()).isEqualTo(expectedAuthor.getName());
  }

  @Test
  @DisplayName("Should display all saved author sorted by Name, descending")
  void getAllAuthors_sortByNameDescending() {

    AuthorEntity author1 = new AuthorEntity(1L, "AAA Author");
    AuthorEntity author2 = new AuthorEntity(2L, "BBB Author2");
    AuthorEntity author3 = new AuthorEntity(3L, "CCC Author3");
    AuthorEntity author4 = new AuthorEntity(4L, "DDD Author3");
    AuthorEntity author5 = new AuthorEntity(5L, "EEE Author3");
    AuthorEntity author6 = new AuthorEntity(6L, "FFF Author3");

    AuthorEntity savedAuthor1 = authorRepository.save(author1);
    AuthorEntity savedAuthor2 = authorRepository.save(author2);
    AuthorEntity savedAuthor3 = authorRepository.save(author3);
    AuthorEntity savedAuthor4 = authorRepository.save(author4);
    AuthorEntity savedAuthor5 = authorRepository.save(author5);
    AuthorEntity savedAuthor6 = authorRepository.save(author6);

    Pageable paging = PageRequest.of(0, 1, Sort.by("name").descending());

    Page<AuthorEntity> pagedAuthorEntity = authorRepository.findAll(paging);

    assertThat(pagedAuthorEntity.get().findFirst()).isEqualTo(
        Optional.of(author6));
  }

  @Test
  @DisplayName("Should display list of author with size 2 and currently in 2nd page sorted by Name ascending")
  void getAuthors_Page2Size2SortByNameAscending() {

    AuthorEntity author1 = new AuthorEntity(1L, "AAA Author");
    AuthorEntity author2 = new AuthorEntity(2L, "BBB Author2");
    AuthorEntity author3 = new AuthorEntity(3L, "CCC Author3");
    AuthorEntity author4 = new AuthorEntity(4L, "DDD Author4");
    AuthorEntity author5 = new AuthorEntity(5L, "EEE Author5");
    AuthorEntity author6 = new AuthorEntity(6L, "FFF Author6");

    AuthorEntity savedAuthor1 = authorRepository.save(author1);
    AuthorEntity savedAuthor2 = authorRepository.save(author2);
    AuthorEntity savedAuthor3 = authorRepository.save(author3);
    AuthorEntity savedAuthor4 = authorRepository.save(author4);
    AuthorEntity savedAuthor5 = authorRepository.save(author5);
    AuthorEntity savedAuthor6 = authorRepository.save(author6);

    int currentPage = 2;
    int pageSize = 2;

    Pageable paging = PageRequest.of(currentPage, pageSize, Sort.by("name"));

    Page<AuthorEntity> pagedAuthorEntity = authorRepository.findAll(paging);

    assertThat(pagedAuthorEntity.getPageable().getPageNumber()).isEqualTo(currentPage);
    assertThat(pagedAuthorEntity.getPageable().getPageSize()).isEqualTo(pageSize);
    assertThat(pagedAuthorEntity.get().findFirst().get().getName()).isEqualTo(author5.getName());

  }


}
