package com.masters.masters.exercise.services;

import com.masters.masters.exercise.exception.RecordNotFoundException;
import com.masters.masters.exercise.model.Author;
import com.masters.masters.exercise.model.AuthorDtoRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AuthorService {

    Page<Author> findAllAuthors(Pageable pageRequest);
    Author saveAuthor(AuthorDtoRequest authorDtoRequest);
    Author findById(Long id) throws RecordNotFoundException;

}
