package com.brownbag.exercise4.repository;

import com.brownbag.exercise4.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends PagingAndSortingRepository<Book, Integer> {

    @Query("SELECT b from Book as b where b.author.id = :authorId AND b.id = :id")
    public Optional<Book> findByIdAndAuthorId(Integer id, Integer authorId);

    @Query("SELECT b from Book as b where b.author.id = :authorId")
    public Page<Book> findBooks(Integer authorId, Pageable page);
}
