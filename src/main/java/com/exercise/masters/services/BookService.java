/**
 * 
 */
package com.exercise.masters.services;

import org.springframework.data.domain.Page;

import com.exercise.masters.exceptions.RecordNotFoundException;
import com.exercise.masters.model.Book;

/**
 * @author michaeldelacruz
 *
 */
public interface BookService {

	public Page<Book> findAllBooks(Long authorId, Integer pageNo, Integer pageSize, String sortBy) throws RecordNotFoundException;
	
	public Book findBookById(Long authorId, Long bookId) throws RecordNotFoundException;
	
	public Book findBookByName(String name) throws RecordNotFoundException;
	
	public Book save(Long authorId, Book book) throws RecordNotFoundException;
	
	public Book deleteById(Long id) throws RecordNotFoundException;
	
}
