package com.java.master.exercise4.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.java.master.exercise4.model.Author;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
	
}
