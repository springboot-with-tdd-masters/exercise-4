package com.masters.masters.exercise.repository;

import com.masters.masters.exercise.model.Author;
import com.masters.masters.exercise.model.BookEntity;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface BookRepository extends PagingAndSortingRepository<BookEntity,Long> {

    List<BookEntity> findByTitleContaining(String title,Pageable pageRequest);
    List<BookEntity> findByAuthor(Author author, Pageable pageRequest);
    List<BookEntity> findByAuthorAndId(Author author,Long id, Pageable pageRequest);
}
