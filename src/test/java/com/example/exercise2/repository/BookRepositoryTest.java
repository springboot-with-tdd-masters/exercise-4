package com.example.exercise2.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.example.exercise2.repository.entity.AuthorEntity;
import com.example.exercise2.repository.entity.BookEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@DataJpaTest
public class BookRepositoryTest {

  @Autowired
  private BookRepository bookRepository;

  @Autowired
  private AuthorRepository authorRepository;

  @Test
  @DisplayName("Save - should accept entity and save details")
  void saveBooks() {

    //setup
    BookEntity book = new BookEntity("Librong Itim", "Sample description",
        new AuthorEntity(1L, "Mr Clean"));

    //act
    BookEntity savedBook = bookRepository.save(book);

    //verify
    assertThat(savedBook).extracting("title", "description", "author")
        .containsExactly(book.getTitle(), book.getDescription(), book.getAuthor());
  }


  @Test
  @DisplayName("List books with title contains string")
  void should_return_list_of_books_with_title_contains_string() {

    AuthorEntity author1 = new AuthorEntity(1L, "Mr Clean");

    AuthorEntity savedAuthor = authorRepository.save(author1);

    BookEntity book1 = new BookEntity(1L, "Librew Brown", "Sample description", savedAuthor);
    BookEntity book2 = new BookEntity(2L, "Librong Black", "Sample description", savedAuthor);
    BookEntity book3 = new BookEntity(3L, "Librong Red", "Sample description", savedAuthor);

    bookRepository.save(book1);
    bookRepository.save(book2);
    bookRepository.save(book3);

    int currentPage = 0;
    int pageSize = 2;
    Pageable paging = PageRequest.of(currentPage, pageSize, Sort.by("id"));

    Page<BookEntity> pagedBookEntity = bookRepository.findByTitleContaining("rong", paging);

    assertThat(pagedBookEntity.getPageable().getPageNumber()).isEqualTo(currentPage);
    assertThat(pagedBookEntity.getPageable().getPageSize()).isEqualTo(pageSize);
    assertThat(pagedBookEntity.get().findFirst().get().getTitle()).isEqualTo(book2.getTitle());
  }
}