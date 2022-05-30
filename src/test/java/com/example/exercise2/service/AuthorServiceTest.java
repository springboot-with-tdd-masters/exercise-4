package com.example.exercise2.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.exercise2.repository.AuthorRepository;
import com.example.exercise2.repository.entity.AuthorEntity;
import com.example.exercise2.service.adapters.AuthorEntityToAuthorAdapter;
import com.example.exercise2.service.adapters.AuthorToAuthorEntityAdapter;
import com.example.exercise2.service.model.Author;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Pageable;

public class AuthorServiceTest {


  @InjectMocks
  AuthorService authorService = new AuthorServiceImpl();

  @Mock
  AuthorRepository authorRepository;

  @Mock
  AuthorToAuthorEntityAdapter authorToAuthorEntityAdapter;

  @Mock
  AuthorEntityToAuthorAdapter authorEntityToAuthorAdapter;

  @Mock
  Pageable mockedPageable;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  @DisplayName("Save should accept author object and save details")
  public void saveAuthor() {

    String author = "Author 1";

    AuthorEntity newAuthorEntity = new AuthorEntity(author);

    AuthorEntity mockedAuthorEntityFromRepo = mock(AuthorEntity.class);
    when(authorRepository.save(newAuthorEntity)).thenReturn(
        mockedAuthorEntityFromRepo);

    Author mockedSavedAuthor = mock(Author.class);
    when(authorEntityToAuthorAdapter.convert(mockedAuthorEntityFromRepo)).thenReturn(
        mockedSavedAuthor);

    Author actualAuthorSaved = authorService.save(author);

    verify(authorRepository).save(newAuthorEntity);
    verify(authorEntityToAuthorAdapter).convert(mockedAuthorEntityFromRepo);

    assertThat(actualAuthorSaved).isEqualTo(mockedSavedAuthor);

  }

  @Test
  @DisplayName("Get author details using author id")
  public void getSingleAuthorById() {

    Author expectedResponse = new Author(1L, "Author 1");

    AuthorEntity mockedAuthorEntityFromRepo = mock(AuthorEntity.class);
    when(authorRepository.findById(1L)).thenReturn(Optional.ofNullable(mockedAuthorEntityFromRepo));

    Author mockedSavedAuthor = mock(Author.class);
    when(authorEntityToAuthorAdapter.convert(mockedAuthorEntityFromRepo)).thenReturn(
        mockedSavedAuthor);

    Author actualAuthorSaved = authorService.findById(1L);

    verify(authorRepository).findById(1L);
    verify(authorEntityToAuthorAdapter).convert(mockedAuthorEntityFromRepo);

    assertThat(actualAuthorSaved).isEqualTo(mockedSavedAuthor);


  }

  @Test
  @DisplayName("List all authors without pagination and sort")
  public void listAllAuthor_noPagingAndSorting() {

    List<AuthorEntity> authorEntityArrayList = new ArrayList<>();

    List<AuthorEntity> spiedAuthorEntityArrayListFromRepository = spy(authorEntityArrayList);
    when(authorRepository.findAll()).thenReturn(spiedAuthorEntityArrayListFromRepository);

    List<Author> authorList = new ArrayList<>();
    List<Author> spiedAuthorList = spy(authorList);

    when(authorEntityToAuthorAdapter.convert(spiedAuthorEntityArrayListFromRepository)).thenReturn(
        spiedAuthorList);

    List<Author> actualAuthorListResult = authorService.findAll();

    verify(authorRepository).findAll();
    verify(authorEntityToAuthorAdapter).convert(spiedAuthorEntityArrayListFromRepository);

    assertThat(actualAuthorListResult).isEqualTo(spiedAuthorList);

  }
}
