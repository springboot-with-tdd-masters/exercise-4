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
import com.exercise.masters.model.Book;
import com.exercise.masters.repository.BookRepository;

/**
 * @author michaeldelacruz
 *
 */

@Service
public class BookServiceImpl implements BookService {
	
	@Autowired
	private BookRepository bookRepository;
	
	@Autowired
	private AuthorService authorService;

	@Override
	public Page<Book> findAllBooks(Long authorId, Integer pageNo, Integer pageSize, String sortBy) throws RecordNotFoundException {
		
		Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
		Page<Book> books = bookRepository.findAll(paging);
		
		if(books.hasContent()) {
			return books;
		} else {
			throw new RecordNotFoundException("No Records Found");
		}
		
	}

	@Override
	public Book findBookById(Long authorId, Long bookId) throws RecordNotFoundException {
		Optional<Book> book = bookRepository.findByIdAndAuthorId(bookId, authorId);
		return book.map(obj -> {
			return obj;
		})
		.orElseThrow(() -> new RecordNotFoundException("No Record found with id: " + bookId));
	}

	@Override
	public Book findBookByName(String name) throws RecordNotFoundException {
		Optional<Book> book = bookRepository.findByName(name);
		return book.map(obj -> {
			return obj;
		})
		.orElseThrow(() -> new RecordNotFoundException("No Record found with name: " + name));
	}

	@Override
	public Book save(Long authorId, Book book) throws RecordNotFoundException {
		Book result = null;
		Book savedBook = null;
		
		Author author = authorService.findAuthorById(authorId);
		
		if(book.getId() != null) {
			savedBook = bookRepository.findById(book.getId()).get();
		}

		if(savedBook == null) {
			book.setAuthor(author);
			result = bookRepository.save(book);
		} else {
			savedBook.setAuthor(author);
			savedBook.setName(book.getName());
			savedBook.setAuthor(book.getAuthor());
			result = bookRepository.save(savedBook);
		}
		return result;
	}

	@Override
	public Book deleteById(Long id) throws RecordNotFoundException {
		Optional<Book> book = bookRepository.findById(id);
		return book.map(obj -> {
			bookRepository.deleteById(obj.getId());
            return obj;
        })
		.orElseThrow(() -> new RecordNotFoundException("No Record found with id: " + id));
	}

}
