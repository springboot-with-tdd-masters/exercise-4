/**
 * 
 */
package com.exercise.masters.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.exercise.masters.exceptions.RecordNotFoundException;
import com.exercise.masters.model.Author;
import com.exercise.masters.repository.AuthorRepository;

/**
 * @author michaeldelacruz
 *
 */

@Service
public class AuthorServiceImpl implements AuthorService {
	
	@Autowired
	private AuthorRepository authorRepository;

	@Override
	public Page<Author> findAllAuthors(Integer pageNo, Integer pageSize, String sortBy) throws RecordNotFoundException {
		Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
		Page<Author> authors = authorRepository.findAll(paging);
		
		if(authors.hasContent()) {
			return authors;
		} else {
			throw new RecordNotFoundException("No Records Found");
		}
	}

	@Override
	public Author findAuthorById(Long id) throws RecordNotFoundException {
		Optional<Author> author = authorRepository.findById(id);
		return author.map(obj -> {
			return obj;
		})
		.orElseThrow(() -> new RecordNotFoundException("No Record found with id: " + id));
	}

	@Override
	public Author findAuthorByName(String name) throws RecordNotFoundException {
		Optional<Author> author = authorRepository.findByName(name);
		return author.map(obj -> {
			return obj;
		})
		.orElseThrow(() -> new RecordNotFoundException("No Record found with name: " + name));
	}

	@Override
	public Author save(Author author) {
		Author result = null;
		Author savedAuthor = null;
		
		if(author.getId() != null) {
			savedAuthor = authorRepository.findById(author.getId()).get();
		}
		
		if(savedAuthor == null) {
			result = authorRepository.save(author);
		} else {
			savedAuthor.setName(author.getName());
			savedAuthor.setBooks(author.getBooks());
			result = authorRepository.save(savedAuthor);
		}
		return result;
	}

	@Override
	public Author deleteById(Long id) throws RecordNotFoundException {
		Optional<Author> author = authorRepository.findById(id);
		return author.map(obj -> {
			authorRepository.deleteById(obj.getId());
            return obj;
        })
		.orElseThrow(() -> new RecordNotFoundException("No Record found with id: " + id));
	}

}
