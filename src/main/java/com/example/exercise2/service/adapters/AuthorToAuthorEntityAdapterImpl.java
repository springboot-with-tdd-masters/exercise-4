package com.example.exercise2.service.adapters;

import com.example.exercise2.repository.entity.AuthorEntity;
import com.example.exercise2.service.model.Author;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class AuthorToAuthorEntityAdapterImpl implements AuthorToAuthorEntityAdapter {

  @Override
  public AuthorEntity convert(Author author) {
    return new AuthorEntity(author.getId(), author.getName(), author.getCreatedDate(),
        author.getLastModifiedDate());
  }

  @Override
  public List<AuthorEntity> convert(List<Author> authorList) {
    List<AuthorEntity> resultList = new ArrayList<>();
    for (Author author : authorList) {
      resultList.add(convert(author));
    }
    return resultList;
  }
}
