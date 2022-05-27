/**
 * 
 */
package com.exercise.masters.services;

import org.springframework.data.domain.Page;

import com.exercise.masters.exceptions.RecordNotFoundException;
import com.exercise.masters.model.Author;

/**
 * @author michaeldelacruz
 *
 */
public interface AuthorService {

	public Page<Author> findAllAuthors(Integer pageNo, Integer pageSize, String sortBy) throws RecordNotFoundException;
	
	public Author findAuthorById(Long id) throws RecordNotFoundException;
	
	public Author findAuthorByName(String name) throws RecordNotFoundException;
	
	public Author save(Author author);
	
	public Author deleteById(Long id) throws RecordNotFoundException;
	
}
