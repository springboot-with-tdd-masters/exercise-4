package com.example.exercisefour.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.exercisefour.exception.LibraryAppException;
import com.example.exercisefour.exception.LibraryAppExceptionCode;
import com.example.exercisefour.model.Author;
import com.example.exercisefour.model.Book;
import com.example.exercisefour.repository.AuthorRepository;
import com.example.exercisefour.repository.BookRepository;
import com.example.exercisefour.response.AuthorResponse;
import com.example.exercisefour.response.BookResponse;
import com.example.exercisefour.service.BookService;

@Service
public class BookServiceImpl implements BookService{
	
	@Autowired
	BookRepository bookRepository;
	
	@Autowired
	AuthorRepository authorRepository;
	
	@Override
	public AuthorResponse createAuthor(Author authorRequest) {
		Author authorEntity = new Author();
		if (authorRequest.getName() != null || !authorRequest.getName().isEmpty()) {
			authorEntity.setName(authorRequest.getName());
			authorRepository.save(authorEntity);
			
			return AuthorResponse.convertToAuthorResponse(authorEntity);
		} else {
			throw new LibraryAppException(LibraryAppExceptionCode.INVALID_NAME_EXCEPTION);
		}
	}

	@Override
	public Page<AuthorResponse> getAllAuthors(Pageable page) {
		return authorRepository.findAll(page).map(AuthorResponse::convertToAuthorResponse);
	}

	@Override
	public AuthorResponse getAuthorById(Long id) {
		Optional<Author> author = authorRepository.findById(id);
		
		if (author.isPresent()) {
			return AuthorResponse.convertToAuthorResponse(author.get());
		} else {
			throw new LibraryAppException(LibraryAppExceptionCode.AUTHOR_NOT_FOUND_EXCEPTION);
		}
	}

	@Override
	public BookResponse createBook(Long id, Book book) {
		Optional<Author> author = authorRepository.findById(id);
		
		if (author.isPresent()) {
			Book newBook = new Book();
			newBook.setTitle(book.getTitle());
			newBook.setDescription(book.getDescription());
			newBook.setAuthor(author.get());
			bookRepository.save(newBook);
			return BookResponse.convertToBookResponse(newBook);
		} else {
			throw new LibraryAppException(LibraryAppExceptionCode.AUTHOR_NOT_FOUND_EXCEPTION);
		}
	}

	@Override
	public Page<BookResponse> getAllBooks(Long id, Pageable page) {
		Optional<Author> author = authorRepository.findById(id);
		
		if (author.isPresent()) {
			return bookRepository.findByAuthorId(id, page).map(BookResponse::convertToBookResponse);
		} else {
			throw new LibraryAppException(LibraryAppExceptionCode.AUTHOR_NOT_FOUND_EXCEPTION);
		}
	}

	@Override
	public Page<BookResponse> getBookById(Long authorId, Long bookId, Pageable page) {
		Optional<Page<Book>> book = Optional.ofNullable(bookRepository.findByAuthorIdAndId(authorId, bookId, page));
		
		if (book.isPresent()) {
			return bookRepository.findByAuthorIdAndId(authorId, bookId, page).map(BookResponse::convertToBookResponse);
		} else {
			throw new LibraryAppException(LibraryAppExceptionCode.RECORD_NOT_FOUND);
		}
	}

	@Override
	public Page<BookResponse> getBookByTitle(String title, Pageable page) {
		return bookRepository.findByTitleContaining(title, page).map(BookResponse::convertToBookResponse);
	}

	@Override
	public BookResponse updateBook(Long authorId, Long bookId, Book book) {
		Optional<Author> author = authorRepository.findById(authorId);
		
		if (author.isPresent()) {
			Optional<Book> bookEntity = bookRepository.findById(bookId);
			if (bookEntity.isPresent()) {
				Book newBook = bookEntity.get();
				newBook.setTitle(book.getTitle());
				newBook.setDescription(book.getDescription());
				bookRepository.save(newBook);
				return BookResponse.convertToBookResponse(newBook);
			} else {
				throw new LibraryAppException(LibraryAppExceptionCode.BOOK_NOT_FOUND_EXCEPTION);
			}
		} else {
			throw new LibraryAppException(LibraryAppExceptionCode.AUTHOR_NOT_FOUND_EXCEPTION);
		}
	}

	public Book deleteByBookId(Long authorId, Long bookId) {
		Optional<Author> author = authorRepository.findById(authorId);
		Optional<Book> book = bookRepository.findById(bookId);
		
		if (author.isPresent()) {
			if (book.isPresent()) {
				bookRepository.deleteById(bookId);
				return book.get();
			} else {
				throw new LibraryAppException(LibraryAppExceptionCode.BOOK_NOT_FOUND_EXCEPTION);
			}
		} else {
			throw new LibraryAppException(LibraryAppExceptionCode.AUTHOR_NOT_FOUND_EXCEPTION);
		}
	}
}
