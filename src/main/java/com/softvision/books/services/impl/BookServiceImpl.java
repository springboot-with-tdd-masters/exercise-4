package com.softvision.books.services.impl;

import com.softvision.books.exeptions.NotFoundException;
import com.softvision.books.repositories.AuthorRepository;
import com.softvision.books.repositories.BookRepository;
import com.softvision.books.repositories.entities.AuthorEntity;
import com.softvision.books.repositories.entities.BookEntity;
import com.softvision.books.services.BookService;
import com.softvision.books.services.converters.BookConverter;
import com.softvision.books.services.domain.Book;
import com.softvision.books.services.domain.BookFilter;
import com.softvision.books.services.domain.Pagination;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class BookServiceImpl implements BookService {

    private BookRepository bookRepository;

    private BookConverter bookConverter;

    private AuthorRepository authorRepository;

    public BookServiceImpl(
            BookRepository bookRepository,
            BookConverter bookConverter,
            AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.bookConverter = bookConverter;
        this.authorRepository = authorRepository;
    }

    @Override
    public Pagination<Book> findAll(BookFilter filter, Pageable pageable) {

        final Page<BookEntity> bookEntityPage;

        if (filter.hasAuthor()) {
            bookEntityPage =  bookRepository.findByAuthorId(filter.getAuthorId(), pageable);
        } else if (filter.hasSearchKey()) {
            bookEntityPage = bookRepository.findByTitleContainingIgnoreCase(filter.getSearchKey().trim().toLowerCase(), pageable);
        } else {
            bookEntityPage = bookRepository.findAll(pageable);
        }

        return bookConverter.convert(bookEntityPage);
    }

    @Override
    public Book findById(Long id) {

        final BookEntity bookEntity = findBookEntityById(id);

        return bookConverter.convert(bookEntity);
    }

    @Override
    public Book add(Long authorId, Book book) {

        final AuthorEntity authorEntity = authorRepository.findById(authorId)
                .orElseThrow(() -> new NotFoundException("Author not found, book must have an Author"));

        final BookEntity bookEntity = bookConverter.convert(book);
        bookEntity.setAuthor(authorEntity);

        final BookEntity savedBook = bookRepository.save(bookEntity);

        return bookConverter.convert(savedBook);
    }

    @Override
    public Book update(Long id, Book book) {

        final BookEntity existingBook = findBookEntityById(id);

        existingBook.setTitle(book.getTitle());
        existingBook.setDescription(book.getDescription());

        bookRepository.save(existingBook);

        return bookConverter.convert(existingBook);
    }

    @Override
    public void deleteById(Long id) {

        final BookEntity existingBook = findBookEntityById(id);

        bookRepository.delete(existingBook);
    }

    private BookEntity findBookEntityById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Book not found"));
    }

}
