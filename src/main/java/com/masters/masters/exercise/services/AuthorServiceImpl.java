package com.masters.masters.exercise.services;

import com.masters.masters.exercise.exception.RecordNotFoundException;
import com.masters.masters.exercise.model.Author;
import com.masters.masters.exercise.model.AuthorDtoRequest;
import com.masters.masters.exercise.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthorServiceImpl implements AuthorService{

    @Autowired
    private AuthorRepository repo;


    @Override
    public Page<Author> findAllAuthors(Pageable pageRequest) {
        Page<Author> page = repo.findAll(pageRequest);
        return  page;
    }

    @Override
    public Author saveAuthor(AuthorDtoRequest authorDtoRequest) {
        Author author = new Author();
        author.setName(authorDtoRequest.getName());
        return repo.save(author);
    }

    @Override
    public Author findById(Long id) throws RecordNotFoundException {
        Optional<Author> author = repo.findById(id);
        if(author.isPresent()){
            return author.get();
        }else{
            throw new RecordNotFoundException("Author not found");
        }
    }
}
