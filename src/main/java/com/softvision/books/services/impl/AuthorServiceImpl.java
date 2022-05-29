package com.softvision.books.services.impl;

import com.softvision.books.exeptions.NotFoundException;
import com.softvision.books.repositories.AuthorRepository;
import com.softvision.books.repositories.entities.AuthorEntity;
import com.softvision.books.services.AuthorService;
import com.softvision.books.services.converters.AuthorConverter;
import com.softvision.books.services.domain.Author;
import com.softvision.books.services.domain.Pagination;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    private final AuthorConverter authorConverter;

    public AuthorServiceImpl(AuthorRepository authorRepository, AuthorConverter authorConverter) {
        this.authorRepository = authorRepository;
        this.authorConverter = authorConverter;
    }

    @Override
    public Pagination<Author> findAll(Pageable pageable) {

        final Page<AuthorEntity> authorEntityPage = authorRepository.findAll(pageable);

        return authorConverter.convert(authorEntityPage);
    }

    @Override
    public Author findById(Long id) {

        final AuthorEntity authorEntity = findAuthorEntityById(id);

        return authorConverter.convert(authorEntity);
    }

    @Override
    public Author save(Author author) {

        final AuthorEntity authorEntity = authorConverter.convert(author);

        final AuthorEntity authorSavedEntity = authorRepository.save(authorEntity);

        return authorConverter.convert(authorSavedEntity);
    }

    @Override
    public Author update(Long id, Author author) {

        final AuthorEntity existingAuthor = findAuthorEntityById(id);

        existingAuthor.setName(author.getName());

        authorRepository.save(existingAuthor);

        return authorConverter.convert(existingAuthor);
    }

    @Override
    public void deleteById(Long id) {

        final AuthorEntity authorEntity = findAuthorEntityById(id);

        authorRepository.delete(authorEntity);
    }

    private AuthorEntity findAuthorEntityById(Long id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Author not found"));
    }

}
