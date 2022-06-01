package com.masters.masters.exercise.services;

import com.masters.masters.exercise.exception.RecordNotFoundException;
import com.masters.masters.exercise.model.Author;
import com.masters.masters.exercise.model.BookDtoRequest;
import com.masters.masters.exercise.model.BookDtoResponse;
import com.masters.masters.exercise.model.BookEntity;
import com.masters.masters.exercise.repository.AuthorRepository;
import com.masters.masters.exercise.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService{

    @Autowired
    private BookRepository repo;

    @Autowired
    private AuthorRepository authorRepository;

    public BookEntity save(BookDtoRequest bookDto,Long id) throws RecordNotFoundException {
        Optional<Author> author = authorRepository.findById(id);
        if(author.isPresent()){
            BookEntity entity = new BookEntity();
            entity.setTitle(bookDto.getTitle());
            entity.setDescription(bookDto.getDescription());
            entity.setAuthor(author.get());
            return repo.save(entity);
        }else{
            throw new RecordNotFoundException("Author not found");
        }
    }

    public Page<BookEntity> findAllBooks(Pageable pageable) {
        return repo.findAll(pageable);
    }

    @Override
    public Page<BookEntity> findByAuthorId(Long id,Pageable pageRequest) throws RecordNotFoundException {
        Optional<Author> author = authorRepository.findById(id);
        if(author.isPresent()){
            List<BookEntity> entity = repo.findByAuthor(author.get(),pageRequest);
            if(!entity.isEmpty()){
                return new PageImpl<>(entity);
            }else{
                throw new RecordNotFoundException("Book not found");
            }
        }else{
            throw new RecordNotFoundException("Author not found");
        }

    }

    @Override
    public Page<BookEntity> findByAuthorIdAndBookId(Long authorId, Long bookId, Pageable pageRequest) throws RecordNotFoundException {
        Optional<Author> author = authorRepository.findById(authorId);
        if(author.isPresent()){
            List<BookEntity> entity = repo.findByAuthorAndId(author.get(),bookId,pageRequest);
            if(!entity.isEmpty()){
                return new PageImpl<>(entity);
            }else{
                throw new RecordNotFoundException("Book not found");
            }
        }else{
            throw new RecordNotFoundException("Author not found");
        }
    }

    @Override
    public Page<BookEntity> findBookByTitle(String title, Pageable pageable) throws RecordNotFoundException {
        List<BookEntity> bookList = repo.findByTitleContaining(title,pageable);
        if(bookList.isEmpty()){
            throw new RecordNotFoundException("Books not found");
        }else{
            return new PageImpl<>(bookList);
        }

    }
}
