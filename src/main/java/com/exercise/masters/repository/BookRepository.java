/**
 * 
 */
package com.exercise.masters.repository;

import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.exercise.masters.model.Book;

/**
 * @author michaeldelacruz
 *
 */

@Repository
public interface BookRepository extends PagingAndSortingRepository<Book, Long>{
	
	public Optional<Book> findByIdAndAuthorId(Long id, Long authorId);
	
	public Optional<Book> findByName(String name);
	
}
