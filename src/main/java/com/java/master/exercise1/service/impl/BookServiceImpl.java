package com.java.master.exercise1.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.java.master.exercise1.exception.RecordNotFoundException;
import com.java.master.exercise1.model.Author;
import com.java.master.exercise1.model.Book;
import com.java.master.exercise1.model.BookRequest;
import com.java.master.exercise1.repository.AuthorRepository;
import com.java.master.exercise1.repository.BookRepository;
import com.java.master.exercise1.service.BookService;

@Service
public class BookServiceImpl implements BookService {

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private AuthorRepository authorRepository;

	@Override
	public Book deleteBookById(Long id) {
		Optional<Book> book = bookRepository.findById(id);
		if (book.isPresent()) {
			bookRepository.deleteById(id);
			return book.get();
		} else {
			throw new RecordNotFoundException("Book not found!");
		}
	}

	@Override
	public Book getBookById(Long id) {
		Optional<Book> book = bookRepository.findById(id);
		if (book.isPresent()) {
			return book.get();
		} else {
			throw new RecordNotFoundException("Book not found!");
		}
	}

	@Override
	public Book createUpdateBook(Book bookRequest) {
		Optional<Book> book = bookRepository.findById(bookRequest.getId());
		if (book.isPresent()) {
			Book updateBook = book.get();
			updateBook.setTitle(bookRequest.getTitle());
			updateBook.setDescription(bookRequest.getDescription());
			updateBook.setAuthor(bookRequest.getAuthor());
			bookRepository.save(bookRequest);
			return updateBook;
		} else {
			bookRepository.save(bookRequest);
			return bookRequest;
		}
	}

	@Override
	public List<Book> getAllBooks() {
		List<Book> books = bookRepository.findAll();
		return books.size() > 0 ? books : new ArrayList<>();
	}

	@Override
	public Book createBook(Long id, Book book) {
		Optional<Author> author = authorRepository.findById(id);
		if (author.isPresent()) {
			book.setAuthor(author.get());
			bookRepository.save(book);
		} else {
			throw new RecordNotFoundException("Author not found!");
		}
		return book;
	}

	@Override
	public Page<Book> getBooksByAuthorId(Long id, Pageable pageable) {
		return bookRepository.findByAuthorId(id, pageable);
	}

	@Override
	public Book getBookByAuthorIdAndByBookId(Long authorId, Long bookId) {
		Optional<Book> books = bookRepository.findByAuthorIdAndId(authorId, bookId);
		if (books.isPresent()) {
			return books.get();
		} else {
			throw new RecordNotFoundException("Record Not Found!");
		}
	}

	@Override
	public Book updateBook(Long authorId, Long bookId, BookRequest bookRequest) {

		Optional<Book> bookData = bookRepository.findByAuthorIdAndId(authorId, bookId);
		if (bookData.isPresent()) {
			Book book = bookData.get();
			book.setTitle(bookRequest.getTitle());
			book.setDescription(bookRequest.getDescription());
			book.getAuthor().setName(bookRequest.getAuthor());
			return bookRepository.save(book);
		} else {
			throw new RecordNotFoundException("Record Not Found!");
		}

	}

	@Override
	public void deleteBookById(Long authorId, Long bookId) {
		Optional<Author> autor = authorRepository.findById(authorId);
		Optional<Book> book = bookRepository.findById(bookId);
		if (autor.isEmpty() || book.isEmpty()) {
			throw new RecordNotFoundException("Record Not Found!");
		}
		bookRepository.deleteById(bookId);
	}

}
