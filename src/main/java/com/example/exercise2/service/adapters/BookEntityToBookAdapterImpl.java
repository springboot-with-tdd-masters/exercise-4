package com.example.exercise2.service.adapters;

import com.example.exercise2.repository.entity.BookEntity;
import com.example.exercise2.service.model.Author;
import com.example.exercise2.service.model.Book;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

@Service
public class BookEntityToBookAdapterImpl implements
    BookEntityToBookAdapter {

  @Override
  public Book convert(BookEntity bookEntity) {
    return new Book(bookEntity.getId(),
        bookEntity.getTitle(),
        bookEntity.getDescription(),
        new Author(bookEntity.getAuthor().getId(),
            bookEntity.getAuthor().getName(),
            bookEntity.getAuthor().getCreatedDate(),
            bookEntity.getAuthor().getLastModifiedDate()),
        bookEntity.getCreatedDate(),
        bookEntity.getLastModifiedDate());
  }

  @Override
  public List<Book> convert(List<BookEntity> bookEntityList) {
    List<Book> resultList = new ArrayList<>();

    for (BookEntity bookEntity : bookEntityList) {
      resultList.add(convert(bookEntity));
    }
    return resultList;
  }

  @Override
  public Page<Book> convert(Page<BookEntity> bookEntityPage) {
    Page<Book> result = new PageImpl<>(convert(bookEntityPage.getContent()),
        bookEntityPage.getPageable(), bookEntityPage.getSize());
    return result;
  }
}
