package com.example.exercise2.service.adapters;

import com.example.exercise2.repository.entity.AuthorEntity;
import com.example.exercise2.service.model.Author;
import java.util.List;

public interface AuthorToAuthorEntityAdapter {

  AuthorEntity convert(Author author);
  List<AuthorEntity> convert(List<Author> authorList);
}
