package com.csv.bookscrudexercise3.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.csv.bookscrudexercise3.exception.RecordNotFoundException;
import com.csv.bookscrudexercise3.model.Author;
import com.csv.bookscrudexercise3.model.Book;
import com.csv.bookscrudexercise3.model.BookUpdate;
import com.csv.bookscrudexercise3.repository.AuthorRepository;
import com.csv.bookscrudexercise3.repository.BookRepository;

@Service
public class BookServiceImpl implements BookService {

	@Autowired
	BookRepository bookRepository;

	@Autowired
	AuthorRepository authorRepository;

	@Override
	public Book createBook(Long id, Book bookEntity) throws RecordNotFoundException {
		Optional<Author> author = authorRepository.findById(id);
		author.orElseThrow(RecordNotFoundException::new);
		bookEntity.setAuthor(author.get());
		return bookRepository.save(bookEntity);
		
	}

	@Override
	public Page<Book> getBooksByAuthorId(Long id, Pageable pageable) {
		return bookRepository.findByAuthorId(id, pageable);
	}

	@Override
	public Book getBookByAuthorIdAndByBookId(Long authorId, Long bookId) throws RecordNotFoundException {
		return bookRepository.findByAuthorIdAndId(authorId, bookId).orElseThrow(RecordNotFoundException::new);
	}

	@Override
	public Page<Book> getBooksByTitleContaining(String title, Pageable pageable) {
		return bookRepository.findByTitleContaining(title, pageable);
	}

	@Override
	public Book updateBook(BookUpdate newBook, Book book) {
		book.setTitle(newBook.getTitle());
		book.setDescription(newBook.getDescription());
		book.getAuthor().setName(newBook.getAuthor());

		return bookRepository.save(book);
	}

	@Override
	public void deleteBookById(Long authorId, Long bookId) throws RecordNotFoundException {
		authorRepository.findById(authorId).orElseThrow(RecordNotFoundException::new);
		bookRepository.findById(bookId).orElseThrow(RecordNotFoundException::new);
		bookRepository.deleteById(bookId);
	}
}
