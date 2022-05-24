package com.softvision.library.tdd.service;

import com.softvision.library.tdd.model.Book;
import com.softvision.library.tdd.model.RecordNotFoundException;
import com.softvision.library.tdd.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    BookRepository repository;

    @Override
    public Book createOrUpdate(final Book entity) {
        return repository.findById(entity.getId()).map(entityToBeUpdated -> {
            entityToBeUpdated.setAuthor(entity.getAuthor());
            entityToBeUpdated.setTitle(entity.getTitle());

            entityToBeUpdated = repository.save(entityToBeUpdated);
            return entityToBeUpdated;
        }).orElseGet(() -> repository.save(entity));
    }

    @Override
    public Page<Book> getAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public List<Book> getAll() {
        return repository.findAll();
    }

    @Override
    public Book getById(long id) {
        return repository.findById(id).orElseThrow(RecordNotFoundException::new);
    }

    @Override
    public Page<Book> getAllByAuthor(long id, Pageable pageable) {
        return repository.findByAuthorId(id, pageable);
    }

    @Override
    public void delete(long id) {
        repository.findById(id).ifPresentOrElse(book -> repository.deleteById(book.getId()), () -> {
            throw new RecordNotFoundException();
        });
    }

    @Override
    public Page<Book> getContainingTitle(String titleInfix, Pageable pageable) {
        Page<Book> result = repository.findByTitleContaining(titleInfix, pageable);
        if (!result.hasContent()) {
            throw new RecordNotFoundException();
        }
        return result;
    }
}
