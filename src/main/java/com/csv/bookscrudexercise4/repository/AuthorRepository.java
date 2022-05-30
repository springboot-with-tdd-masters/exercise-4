package com.csv.bookscrudexercise4.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.csv.bookscrudexercise4.model.Author;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long>{
}
