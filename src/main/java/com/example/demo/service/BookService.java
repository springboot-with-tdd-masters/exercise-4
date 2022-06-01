package com.example.demo.service;

import com.example.demo.exception.BookNotFoundException;
import com.example.demo.model.Author;
import com.example.demo.model.Book;
import com.example.demo.model.BookRequest;
import com.example.demo.repository.AuthorRepository;
import com.example.demo.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    public Long createNewBook(BookRequest bookRequest){
        List<Author> fetchedAuthors = authorRepository.findByNameIgnoreCase(bookRequest.getAuthor());

        Book book = new Book();
        if(CollectionUtils.isEmpty(fetchedAuthors)){
            book.setAuthor(authorRepository.save(buildAuthor(bookRequest.getAuthor())));
        }else{
            book.setAuthor(fetchedAuthors.get(0)); //since this is just a demo, no further validation
        }

        book.setTitle(bookRequest.getTitle());
        Book newBook = bookRepository.save(book);

        return newBook.getId();
    }

    public Book readBookById(Long id){
        Optional<Book> requestedBook = bookRepository.findById(id);
        if(requestedBook.isEmpty()){
            throw new BookNotFoundException(String.format("Book with id %s not found", id));
        }
        return requestedBook.get();
    }

    public Page<Book> readBookByTitle(String title, Pageable pageable){
        Page<Book> requestedBook = bookRepository.findByTitleContainingIgnoreCase(title, pageable);
        if(requestedBook.isEmpty()){
            throw new BookNotFoundException(String.format("Book with title %s not found", title));
        }
        return requestedBook;
    }

    public Page<Book> readAllBooks(Pageable pageable){
        return bookRepository.findAll(pageable);
    }

    private Author buildAuthor(String authorName) {
        return Author.builder().name(authorName).build();
    }

    @Transactional
    public Book updateBook(Long bookId, BookRequest bookRequest) {
        Optional<Book> retrievedBook = bookRepository.findById(Long.valueOf(bookId));
        if (retrievedBook.isEmpty()) {
            throw new BookNotFoundException(String.format("Book with id %s not found", bookId));
        }
        Book bookToBeUpdated = retrievedBook.get();
        bookToBeUpdated.setAuthor(authorRepository.save(buildAuthor(bookRequest.getAuthor())));
        bookToBeUpdated.setTitle(bookRequest.getTitle());
        return bookRepository.save(bookToBeUpdated);
    }

    public void deleteBookById(Long id) {
        bookRepository.deleteById(id);
    }
}
