package com.example.exercise4.service.adapters;

import com.example.exercise4.repository.entity.AuthorEntity;
import com.example.exercise4.service.model.Author;
import java.util.List;
import org.springframework.data.domain.Page;

public interface AuthorEntityToAuthorAdapter {
  Author convert(AuthorEntity mockedAuthorEntityFromRepo);

  List<Author> convert(List<AuthorEntity> authorEntityList);

  Page<Author> convert(Page<AuthorEntity> authorEntityPage);
}
