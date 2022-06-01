package com.masters.masters.exercise.services;

import com.masters.masters.exercise.exception.RecordNotFoundException;
import com.masters.masters.exercise.model.Author;
import com.masters.masters.exercise.model.BookDtoRequest;
import com.masters.masters.exercise.model.BookDtoResponse;
import com.masters.masters.exercise.model.BookEntity;
import com.masters.masters.exercise.repository.AuthorRepository;
import com.masters.masters.exercise.repository.BookRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class BookServiceTest {

    @Mock
    BookRepository repo;

    @Mock
    AuthorRepository authorRepository;

    @InjectMocks
    BookServiceImpl bookService;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void saveHappyPath() throws RecordNotFoundException {
        BookDtoRequest bookDto = new BookDtoRequest();
        bookDto.setTitle("title1");
        bookDto.setDescription("description1");
        Author author = new Author();
        author.setName("author1");
        author.setId(Long.parseLong("1"));
        BookEntity repoResponse = new BookEntity();
        repoResponse.setTitle("title1");
        repoResponse.setDescription("description1");
        repoResponse.setAuthor(author);
        when(authorRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(author));
        when(repo.save(Mockito.any(BookEntity.class))).thenReturn(repoResponse);
        BookEntity bookDtoResponse = bookService.save(bookDto,Long.parseLong("1"));
        verify(repo).save(Mockito.any(BookEntity.class));
        Assertions.assertEquals("title1",bookDtoResponse.getTitle());
    }

    @Test
    public void findAllHappyPath(){
        BookEntity book1 = new BookEntity();
        book1.setId(1L);
        book1.setTitle("title1");
        BookEntity book2 = new BookEntity();
        book1.setId(2L);
        book2.setTitle("title2");
        Pageable pageable = PageRequest.of(0,5, Sort.by("title").ascending());
        when(repo.findAll(pageable)).thenReturn(new PageImpl<>(List.of(book1,book2)));

        Page<BookEntity> entity = bookService.findAllBooks(pageable);
        verify(repo).findAll(pageable);
        assertThat(entity.getContent()).hasSize(2);
    }

    @Test
    public void findAllNoRecord(){
        Pageable pageable = PageRequest.of(0,5, Sort.by("title").ascending());
        when(repo.findAll(pageable)).thenReturn(new PageImpl<>(List.of()));
        Page<BookEntity> bookPage = bookService.findAllBooks(pageable);
        verify(repo).findAll(pageable);
        assertThat(bookPage).isEmpty();
    }

    @Test
    public void findByAuthorId() throws RecordNotFoundException {
        Author author = new Author();
        author.setId(Long.parseLong("1"));
        author.setName("name");
        BookEntity book1 = new BookEntity();
        book1.setId(1L);
        book1.setTitle("title1");
        BookEntity book2 = new BookEntity();
        book1.setId(2L);
        book2.setTitle("title2");
        Pageable pageable = PageRequest.of(0,20);
        when(authorRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(author));
        when(repo.findByAuthor(Mockito.any(Author.class),Mockito.any())).thenReturn(List.of(book1,book2));
        Page<BookEntity> bookPage = bookService.findByAuthorId(Long.parseLong("1"),pageable);
        verify(authorRepository).findById(Long.parseLong("1"));
        verify(repo).findByAuthor(author,pageable);
        Assertions.assertEquals(2,bookPage.getContent().size());
    }

    @Test
    public void findByAuthorIdAndBookId() throws RecordNotFoundException {
        Author author = new Author();
        author.setId(Long.parseLong("1"));
        author.setName("name");
        BookEntity book1 = new BookEntity();
        book1.setId(1L);
        book1.setTitle("title1");
        BookEntity book2 = new BookEntity();
        book1.setId(2L);
        book2.setTitle("title2");
        Pageable pageable = PageRequest.of(0,20);
        when(authorRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(author));
        when(repo.findByAuthorAndId(Mockito.any(Author.class),Mockito.anyLong(),Mockito.any())).thenReturn(List.of(book1));
        Page<BookEntity> bookPage = bookService.findByAuthorIdAndBookId(Long.parseLong("1"),1L,pageable);
        verify(authorRepository).findById(Long.parseLong("1"));
        verify(repo).findByAuthorAndId(author,1L,pageable);
        Assertions.assertEquals(1,bookPage.getContent().size());
    }

    @Test
    public void findBookByTitle() throws RecordNotFoundException {
        BookEntity book1 = new BookEntity();
        book1.setId(1L);
        book1.setTitle("title1");
        BookEntity book2 = new BookEntity();
        book1.setId(2L);
        book2.setTitle("title2");
        Pageable pageable = PageRequest.of(0,20);
        when(repo.findByTitleContaining(Mockito.anyString(),Mockito.any())).thenReturn(List.of(book1,book2));
        Page<BookEntity> bookPage = bookService.findBookByTitle("ti",pageable);
        verify(repo).findByTitleContaining("ti",pageable);
        Assertions.assertEquals(2,bookPage.getContent().size());
    }
}
