package com.example.exercise4.service.adapters;

import com.example.exercise4.repository.entity.AuthorEntity;
import com.example.exercise4.service.model.Author;
import java.util.List;

public interface AuthorToAuthorEntityAdapter {

  AuthorEntity convert(Author author);
  List<AuthorEntity> convert(List<Author> authorList);
}
