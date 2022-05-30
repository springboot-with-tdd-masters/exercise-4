package com.brownbag.exercise4.repository;

import com.brownbag.exercise4.entity.Author;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends PagingAndSortingRepository<Author, Integer> {
}
