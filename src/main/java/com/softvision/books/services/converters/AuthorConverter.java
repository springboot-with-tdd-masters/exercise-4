package com.softvision.books.services.converters;

import com.softvision.books.repositories.entities.AuthorEntity;
import com.softvision.books.services.domain.Author;
import com.softvision.books.services.domain.Pagination;
import org.springframework.data.domain.Page;

public interface AuthorConverter {

    AuthorEntity convert(Author author);

    Author convert(AuthorEntity authorEntity);

    Pagination<Author> convert(Page<AuthorEntity> page);

}
