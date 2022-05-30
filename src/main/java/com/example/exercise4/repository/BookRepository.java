package com.example.exercise4.repository;

import com.example.exercise4.repository.entity.BookEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<BookEntity, Long> {

  Page<BookEntity> findByTitleContaining(String title, Pageable pageable);
}
