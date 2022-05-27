package com.example.exercise2.service;

import com.example.exercise2.service.model.Author;
import java.util.List;
import org.springframework.data.domain.Page;

public interface AuthorService {

  Author save(String authorName);

  Author findById(long l);

  List<Author> findAll();

  Page<Author> findAll(int page, int size, String[] sort);
}
