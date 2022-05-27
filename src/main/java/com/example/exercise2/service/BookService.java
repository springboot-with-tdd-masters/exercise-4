package com.example.exercise2.service;

import com.example.exercise2.service.model.Book;
import java.util.List;
import org.springframework.data.domain.Page;

public interface BookService {

  Book save(Book book);

  List<Book> findAll();

  Page<Book> findAll(int page, int size, String[] sort);

  Page<Book> findBooksByTitleContaining(String title, int page, int size, String[] sort);

  Book findById(long l);
}
