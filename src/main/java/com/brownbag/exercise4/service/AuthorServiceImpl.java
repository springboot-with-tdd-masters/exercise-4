package com.brownbag.exercise4.service;

import com.brownbag.exercise4.entity.Author;
import com.brownbag.exercise4.errorhandler.AuthorNotFoundException;
import com.brownbag.exercise4.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthorServiceImpl implements AuthorService{

    @Autowired
    private AuthorRepository authorRepository;

    @Override
    public Author save(Author author) {
        return authorRepository.save(author);
    }

    @Override
    public Author update(Integer id, Author author) throws AuthorNotFoundException {
        Optional<Author> optionalCurrentAuthor = authorRepository.findById(id);

        if (optionalCurrentAuthor.isEmpty()) {
            throw new AuthorNotFoundException();
        }

        Author currentAuthor = optionalCurrentAuthor.get();
        currentAuthor.setName(currentAuthor.getName());

        return authorRepository.save(currentAuthor);
    }

    @Override
    public Author delete(Integer id) throws AuthorNotFoundException {
        Optional<Author> optionalCurrentAuthor = authorRepository.findById(id);

        if (optionalCurrentAuthor.isEmpty()) {
            throw new AuthorNotFoundException();
        }

        Author currentAuthor = optionalCurrentAuthor.get();
        authorRepository.deleteById(id);

        return currentAuthor;
    }

    @Override
    public Author findById(Integer id) throws AuthorNotFoundException {
        Optional<Author> optionalCurrentAuthor = authorRepository.findById(id);

        if (optionalCurrentAuthor.isEmpty()) {
            throw new AuthorNotFoundException();
        }

        return optionalCurrentAuthor.get();
    }

    @Override
    public Page<Author> find(Integer page, Integer size, Sort sort)  {
        return authorRepository.findAll(PageRequest.of(page, size, sort));
    }
}
