package com.example.exercise2.service;

import com.example.exercise2.exception.AuthorNotFoundException;
import com.example.exercise2.repository.AuthorRepository;
import com.example.exercise2.repository.entity.AuthorEntity;
import com.example.exercise2.service.adapters.AuthorEntityToAuthorAdapter;
import com.example.exercise2.service.adapters.AuthorToAuthorEntityAdapter;
import com.example.exercise2.service.model.Author;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

@Service
public class AuthorServiceImpl implements AuthorService {

  @Autowired
  private AuthorRepository authorRepository;

  @Autowired
  private AuthorEntityToAuthorAdapter authorEntityToAuthorAdapter;

  @Autowired
  private AuthorToAuthorEntityAdapter authorToAuthorEntityAdapter;

  @Override
  public Author save(String authorName) {
    AuthorEntity savedAuthorEntity = authorRepository.save(new AuthorEntity(authorName));
    return authorEntityToAuthorAdapter.convert(savedAuthorEntity);
  }

  @Override
  public Author findById(long authorId) {

    Optional<AuthorEntity> authorEntity = authorRepository.findById(authorId);

    if (authorEntity.isPresent()) {
      return authorEntityToAuthorAdapter.convert(authorEntity.get());
    } else {
      throw new AuthorNotFoundException();
    }
  }

  @Override
  public List<Author> findAll() {
    return authorEntityToAuthorAdapter.convert(authorRepository.findAll());
  }

  @Override
  public Page<Author> findAll(int page, int size, String[] sort) {

    Pageable pagingSort = PageRequest.of(page, size, Sort.by(getSortOrder(sort)));

    Page<AuthorEntity> authorEntityPage = authorRepository.findAll(pagingSort);

    return authorEntityToAuthorAdapter.convert(authorEntityPage);
  }


  private List<Order> getSortOrder(String[] sort) {
    List<Order> orders = new ArrayList<Order>();
    if (sort[0].contains(",")) {
      for (String sortOrder : sort) {
        String[] _sort = sortOrder.split(",");
        orders.add(new Order(getSortDirection(_sort[1]), _sort[0]));
      }
    } else {
      orders.add(new Order(getSortDirection(sort[1]), sort[0]));
    }
    return orders;
  }

  private Sort.Direction getSortDirection(String direction) {
    if (direction.equals("asc")) {
      return Sort.Direction.ASC;
    } else if (direction.equals("desc")) {
      return Sort.Direction.DESC;
    }
    return Sort.Direction.ASC;
  }


}
