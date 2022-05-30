package com.example.exercise4.service.adapters;

import com.example.exercise4.repository.entity.AuthorEntity;
import com.example.exercise4.repository.entity.BookEntity;
import com.example.exercise4.service.model.Book;
import org.springframework.stereotype.Service;

@Service
public class BookToBookEntityAdapterImpl implements
    BookToBookEntityAdapter {

  @Override
  public BookEntity convert(Book book) {
    return new BookEntity(book.getId(), book.getTitle(),
        book.getDescription(),
        new AuthorEntity(book.getAuthor().getId(), book.getAuthor().getName()));
  }
}
