package com.example.exercise2.service.adapters;

import com.example.exercise2.repository.entity.AuthorEntity;
import com.example.exercise2.service.model.Author;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

@Service
public class AuthorEntityToAuthorAdapterImpl implements AuthorEntityToAuthorAdapter {

  @Override
  public Author convert(AuthorEntity authorEntity) {
    return new Author(authorEntity.getId(), authorEntity.getName(), authorEntity.getCreatedDate(),
        authorEntity.getLastModifiedDate());
  }

  @Override
  public List<Author> convert(List<AuthorEntity> authorEntityList) {
    List<Author> resultList = new ArrayList<>();
    for (AuthorEntity authorEntity : authorEntityList) {
      resultList.add(convert(authorEntity));
    }
    return resultList;
  }

  @Override
  public Page<Author> convert(Page<AuthorEntity> authorEntityPage) {
    Page<Author> result = new PageImpl<>(convert(authorEntityPage.getContent()),
        authorEntityPage.getPageable(), authorEntityPage.getSize());
    return result;
  }
}
