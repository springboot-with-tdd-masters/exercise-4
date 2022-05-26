package com.brownbag.exercise4.service;

import com.brownbag.exercise4.entity.Author;
import com.brownbag.exercise4.errorhandler.AuthorNotFoundException;
import com.brownbag.exercise4.errorhandler.BookNotFoundException;
import com.brownbag.exercise4.entity.Book;
import com.brownbag.exercise4.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorService authorService;

    @Override
    public Book save(Integer authorId, Book book) throws AuthorNotFoundException {
        Author author = authorService.findById(authorId);
        book.setAuthor(author);
        bookRepository.save(book);

        return book;
    }

    @Override
    public Book update(Integer authorId, Integer id, Book book) throws BookNotFoundException
            , AuthorNotFoundException  {
        Author author = authorService.findById(authorId);

        Optional<Book> optionalCurrentBook = bookRepository.findById(id);

        if (optionalCurrentBook.isEmpty()) {
            throw new BookNotFoundException();
        }

        Book currentBook = optionalCurrentBook.get();
        currentBook.setTitle(book.getTitle());
        currentBook.setDescription(book.getDescription());
        currentBook.setDatePublished(book.getDatePublished());
        currentBook.setAuthor(author);

        bookRepository.save(currentBook);

        return currentBook;
    }

    @Override
    @Transactional
    public Book delete(Integer id) throws BookNotFoundException{

        Optional<Book> optionalCurrentBook = bookRepository.findById(id);

        if (optionalCurrentBook.isEmpty()) {
            throw new BookNotFoundException();
        }

        Book currentBook = optionalCurrentBook.get();
        bookRepository.deleteById(id);

        return currentBook;
    }

    @Override
    public Book findById(Integer authorId, Integer id) throws BookNotFoundException {
        Optional<Book> optionalCurrentBook = bookRepository.findByIdAndAuthorId(id,
                authorId);

        if (optionalCurrentBook.isEmpty()) {
            throw new BookNotFoundException();
        }

        return optionalCurrentBook.get();
    }

    @Override
    public Page<Book> find(Integer authorId, Integer page, Integer size, Sort sort){
        return bookRepository.findBooks(authorId, PageRequest.of(page, size, sort));
    }
}
