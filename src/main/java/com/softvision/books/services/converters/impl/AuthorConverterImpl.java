package com.softvision.books.services.converters.impl;

import com.softvision.books.repositories.entities.AuthorEntity;
import com.softvision.books.services.converters.AuthorConverter;
import com.softvision.books.services.domain.Author;
import com.softvision.books.services.domain.PageBean;
import com.softvision.books.services.domain.Pagination;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AuthorConverterImpl implements AuthorConverter {

    @Override
    public AuthorEntity convert(Author author) {

        final AuthorEntity authorEntity = new AuthorEntity();

        authorEntity.setId(author.getId());
        authorEntity.setName(author.getName());

        return authorEntity;
    }

    @Override
    public Author convert(AuthorEntity authorEntity) {

        final Author author = new Author();

        author.setId(authorEntity.getId());
        author.setName(authorEntity.getName());
        author.setCreatedAt(String.valueOf(authorEntity.getCreatedDate()));
        author.setUpdatedAt(String.valueOf(authorEntity.getLastModifiedDate()));

        return author;
    }

    @Override
    public Pagination<Author> convert(Page<AuthorEntity> page) {

        final List<Author> authors = page.get()
                .map(this::convert)
                .collect(Collectors.toList());

        final Pageable pageable = page.getPageable();

        final PageBean pageBean = new PageBean.Builder()
                .page(pageable.getPageNumber())
                .maxPage(page.getTotalPages())
                .size(pageable.getPageSize())
                .total(page.getTotalElements())
                .build();

        return Pagination.of(authors, pageBean);
    }
}
