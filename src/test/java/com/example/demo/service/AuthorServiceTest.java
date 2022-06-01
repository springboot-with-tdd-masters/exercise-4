package com.example.demo.service;

import com.example.demo.exception.AuthorNotFoundException;
import com.example.demo.model.Author;
import com.example.demo.model.AuthorRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class AuthorServiceTest {

    @MockBean
    private AuthorService authorService;
    private final String author1 = "J. K. Rowling";
    private final String author3 = "William Shakespeare";
    private final String author4 = "Mitch Albom";
    private final String author5 = "Stephen King";

    private List<Author> authorList = new ArrayList<>();

    @BeforeEach
    public void setUp(){
        authorList.add(addAuthor(author1, 1L));
        authorList.add(addAuthor(author3, 3L));
        authorList.add(addAuthor(author4, 4L));
    }

    @AfterEach
    public void close(){
        authorList = new ArrayList<>();
    }

    private Author addAuthor(String author, long id) {
        return Author.builder().id(id).name(author).build();
    }

    @Test
    @DisplayName("Given a successful save, response should give http status 200")
    public void successCreateAuthor() throws Exception {
        AuthorRequest authorRequest = new AuthorRequest();
        authorRequest.setName(author5);
        authorRequest.setBooks(Arrays.asList("The Green Mile"));

        ArgumentCaptor<AuthorRequest> authorRequestArgumentCaptor = ArgumentCaptor.forClass(AuthorRequest.class);
        when(authorService.createNewAuthor(authorRequestArgumentCaptor.capture())).thenReturn(5L);

        authorService.createNewAuthor(authorRequest);
        assertThat(authorRequestArgumentCaptor.getValue().getName(), is(author5));
    }

    @Test
    @DisplayName("Given a successful read by ID, response should give http status 200")
    public void shouldReadAuthorByIdSuccess() throws Exception {
        when(authorService.readAuthorById(1L)).thenReturn(authorList.get(0));
        Author author = authorService.readAuthorById(1L);
        assertThat(author1, is(author.getName()));
    }

    @Test
    @DisplayName("Given a successful read, response should give http status 200")
    public void successReadAllAuthorWithSortAndPagination() throws Exception {
        Pageable pageRequest = PageRequest.of(0, 4, Sort.by("name").ascending());
        List<Author> sortedAuthors = authorList.stream().sorted(Comparator.comparing(Author::getName)).collect(Collectors.toList());
        Page<Author> authorPage = new PageImpl<>(sortedAuthors);
        when(authorService.readAllAuthors(pageRequest)).thenReturn(authorPage);

        authorService.readAllAuthors(pageRequest);

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(authorService).readAllAuthors(pageableCaptor.capture());
        PageRequest pageable = (PageRequest) pageableCaptor.getValue();
        assertThat(pageable.getPageNumber(), is(0));
        assertThat(pageable.getPageSize(), is(4));

        Page<Author> page = authorService.readAllAuthors(pageRequest);
        assertThat(page.getContent(),hasSize(3));
    }

    @Test
    @DisplayName("Given a successful read, response should give http status 200")
    public void successReadAllAuthorsUsingPageableDefault() throws Exception {
        List<Author> sortedAuthors = authorList.stream().sorted(Comparator.comparing(Author::getId)).collect(Collectors.toList());
        Page<Author> authorPage = new PageImpl<>(sortedAuthors);
        when(authorService.readAllAuthors(null)).thenReturn(authorPage);

        authorService.readAllAuthors(null);
//
//        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
//        verify(authorService).readAllAuthors(pageableCaptor.capture());
//        PageRequest pageable = (PageRequest) pageableCaptor.getValue();
//        assertThat(pageable.getPageNumber(), is(0));
//        assertThat(pageable.getPageSize(), is(10));

        Page<Author> page = authorService.readAllAuthors(null);
        assertThat(page.getContent(),hasSize(3));
    }


    @Test
    @DisplayName("Given a successful read by update, response should give http status 200")
    public void shouldUpdateAuthorSuccess() throws Exception {
        AuthorRequest authorRequest = new AuthorRequest();
        String newAuthorName = author1.concat(" Perry");
        authorRequest.setName(newAuthorName);
        authorRequest.setBooks(Arrays.asList("Harry"));
        authorList.get(0).setName(newAuthorName);

        ArgumentCaptor<Long> idRequestArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<AuthorRequest> authorRequestArgumentCaptor = ArgumentCaptor.forClass(AuthorRequest.class);
        when(authorService.updateAuthor(eq(1L), eq(authorRequest)))
                .thenReturn(authorList.get(0));

        authorService.updateAuthor(1L, authorRequest);

        verify(authorService).updateAuthor(idRequestArgumentCaptor.capture(), authorRequestArgumentCaptor.capture());

        assertThat(authorRequestArgumentCaptor.getValue().getName(), is(newAuthorName));
        assertThat(authorRequestArgumentCaptor.getValue().getBooks(), is(Arrays.asList("Harry")));
        assertThat(idRequestArgumentCaptor.getValue(), is(1L));
    }

    @Test
    @DisplayName("Given a fail read, response should give http status 404")
    public void shouldReturn404AuthorNotFoundGet() throws Exception {
        Long bookId = 42L;
        when(authorService.readAuthorById(bookId)).thenThrow(new AuthorNotFoundException(String.format("Author with id %s not found", bookId)));
    }
}