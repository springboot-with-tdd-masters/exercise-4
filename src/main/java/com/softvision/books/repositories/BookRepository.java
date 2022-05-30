package com.softvision.books.repositories;

import com.softvision.books.repositories.entities.BookEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<BookEntity, Long> {

    Page<BookEntity> findByAuthorId(Long authorId, Pageable pageable);

    Page<BookEntity> findByTitleContainingIgnoreCase(String title, Pageable pageable);

}
