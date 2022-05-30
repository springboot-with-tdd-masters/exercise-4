package com.example.bookTDD.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.bookTDD.exception.RecordNotFoundException;
import com.example.bookTDD.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.bookTDD.model.Book;

@Service
public class BookService {

	@Autowired
	BookRepository bookRepository;

	public Page<Book> getAllBooks(Pageable pageable) {
		return bookRepository.findAll(pageable);
	}

	public Optional<Book> getBookById(Long id) {
		return bookRepository.findById(id);
	}

	public Book createOrUpdateBook(Book entity) throws RecordNotFoundException {
		Optional<Book> book = getBookById(entity.getId());
		if (book.isPresent()) {
			Book newEntity = book.get();
			newEntity.setTitle(entity.getTitle());
			newEntity = bookRepository.save(newEntity);
			return newEntity;
		} else {
			entity = bookRepository.save(entity);
			return entity;
		}
	}

	public void deleteByUserId(Long id) throws RecordNotFoundException {
		Optional<Book> book = getBookById(id);
		if (book.isPresent()) {
			bookRepository.deleteById(id);
		} else {
			throw new RecordNotFoundException("No book record exist for given id");
		}
	}

}
