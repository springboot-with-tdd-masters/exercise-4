package com.example.exercise4.service.adapters;

import com.example.exercise4.repository.entity.BookEntity;
import com.example.exercise4.service.model.Book;
import java.util.List;
import org.springframework.data.domain.Page;

public interface BookEntityToBookAdapter {

  Book convert(BookEntity bookEntity);

  List<Book> convert(List<BookEntity> bookEntity);

  Page<Book> convert(Page<BookEntity> bookEntityPage);
}
