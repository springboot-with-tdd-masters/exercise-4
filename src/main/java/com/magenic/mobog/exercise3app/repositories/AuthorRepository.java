package com.magenic.mobog.exercise3app.repositories;

import com.magenic.mobog.exercise3app.entities.Author;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    List<Author> findAll();
    Page<Author> findAll(Pageable page);
}
