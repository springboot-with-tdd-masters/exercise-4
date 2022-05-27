package com.example.exercise2.service.adapters;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.exercise2.repository.entity.AuthorEntity;
import com.example.exercise2.repository.entity.BookEntity;
import com.example.exercise2.service.model.Author;
import com.example.exercise2.service.model.Book;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

public class BookEntityToBookAdapterTest {

  @InjectMocks
  BookEntityToBookAdapter bookEntityToBookAdapter = new BookEntityToBookAdapterImpl();


  @Test
  @DisplayName("Convert BookEntity object to Book object")
  void convert_BookEntity_to_Book_test() {

    BookEntity bookEntity = new BookEntity(1l, "Science 1", "Sample description",
        new AuthorEntity(1L, "Mr Clean"));
    Book expectedBookObject = new Book(1l, "Science 1", "Sample description",
        new Author(1L, "Mr Clean"));

    Book actualBookObject = bookEntityToBookAdapter.convert(bookEntity);

    assertThat(actualBookObject.toString()).isEqualTo(expectedBookObject.toString());


  }

  @Test
  @DisplayName("Convert List of BookEntity object to List of Book object")
  void convert_List_BookEntity_to_List_Book_test() {
    List<BookEntity> bookEntityList = Arrays.asList(
        new BookEntity(1l, "Science 1", "Sample description", new AuthorEntity(1L, "Mr Clean1")),
        new BookEntity(2l, "Science 2", "Sample description", new AuthorEntity(2L, "Mr Clean2")),
        new BookEntity(3l, "Science 3", "Sample description", new AuthorEntity(3L, "Mr Clean3")),
        new BookEntity(4l, "Science 4", "Sample description", new AuthorEntity(4L, "Mr Clean4"))
    );

    List<BookEntity> expectedBookList = Arrays.asList(
        new BookEntity(1l, "Science 1", "Sample description", new AuthorEntity(1L, "Mr Clean1")),
        new BookEntity(2l, "Science 2", "Sample description", new AuthorEntity(2L, "Mr Clean2")),
        new BookEntity(3l, "Science 3", "Sample description", new AuthorEntity(3L, "Mr Clean3")),
        new BookEntity(4l, "Science 4", "Sample description", new AuthorEntity(4L, "Mr Clean4"))
    );

    List<Book> actualBookList = bookEntityToBookAdapter.convert(bookEntityList);

    assertThat(actualBookList.size()).isEqualTo(expectedBookList.size());

  }
}
