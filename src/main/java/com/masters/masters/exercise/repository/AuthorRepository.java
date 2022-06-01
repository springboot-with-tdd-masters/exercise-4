package com.masters.masters.exercise.repository;

import com.masters.masters.exercise.model.Author;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends PagingAndSortingRepository<Author,Long> {


}
