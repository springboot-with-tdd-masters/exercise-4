package com.example.basicauth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.basicauth.model.Author;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long>{

}
