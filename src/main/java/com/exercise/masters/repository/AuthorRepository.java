/**
 * 
 */
package com.exercise.masters.repository;

import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.exercise.masters.model.Author;

/**
 * @author michaeldelacruz
 *
 */

@Repository
public interface AuthorRepository extends PagingAndSortingRepository<Author, Long>{

	public Optional<Author> findByName(String name);
	
}
