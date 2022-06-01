package com.example.demo.service;

import com.example.demo.exception.AuthorNotFoundException;
import com.example.demo.model.Author;
import com.example.demo.model.AuthorRequest;
import com.example.demo.model.Book;
import com.example.demo.repository.AuthorRepository;
import com.example.demo.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class AuthorService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    public Long createNewAuthor(AuthorRequest authorRequest) {
        Author newAuthor = authorRepository.save(buildAuthor(authorRequest));
        return newAuthor.getId();
    }

    public Author readAuthorById(Long id) {
        Optional<Author> requestedAuthor = authorRepository.findById(id);
        if (requestedAuthor.isEmpty()) {
            throw new AuthorNotFoundException(String.format("Author with id %s not found", id));
        }
        return requestedAuthor.get();
    }

    public Page<Author> readAllAuthors(Pageable pageable) {
        return authorRepository.findAll(pageable);
    }

    @Transactional
    public Author updateAuthor(Long authorId, AuthorRequest authorRequest) {
        Optional<Author> requestedAuthor = authorRepository.findById(Long.valueOf(authorId));
        if (requestedAuthor.isEmpty()) {
            throw new AuthorNotFoundException(String.format("Author with id %s not found", authorId));
        }
        Author authorToBeUpdated = requestedAuthor.get();
        //insert update Books
        authorToBeUpdated.setName(authorRequest.getName());
        return authorRepository.save(authorToBeUpdated);
    }


    public void deleteAuthorById(Long id) {
        authorRepository.deleteById(id);
    }

    private Author buildAuthor(AuthorRequest authorRequest) {
        return Author.builder().name(authorRequest.getName()).books(buildBooks(authorRequest.getBooks())).build();
    }

    private Set<Book> buildBooks(List<String> booksFromRequest) {
        Set<Book> books = new HashSet<>();
        books.forEach(b -> books.add(buildBook(b.getTitle())));
        return books;
    }

    private Book buildBook(String bookTitle) {
        return bookRepository.save(Book.builder().title(bookTitle).build());
    }
}
