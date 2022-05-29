package com.softvision.books.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softvision.books.exeptions.NotFoundException;
import com.softvision.books.services.AuthorService;
import com.softvision.books.services.domain.Author;
import com.softvision.books.services.domain.PageBean;
import com.softvision.books.services.domain.Pagination;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthorController.class)
public class AuthorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthorService authorService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("findAllEndpoint_shouldFoundPaginatedAuthors")
    void findAllEndpoint_shouldReturnPaginatedAuthors() throws Exception {
        // Arrange
        List<Author> authors = Arrays.asList(new Author("James"), new Author("Rod"));
        final PageBean pageBean = new PageBean.Builder()
                .page(0)
                .size(2)
                .build();

        final Pagination<Author> paginatedAuthor = Pagination.of(authors, pageBean);

        when(authorService.findAll(PageRequest.of(0, 2)))
                .thenReturn(paginatedAuthor);

        // Act
        mockMvc.perform(get("/api/rest/authors?page=0&size=2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(paginatedAuthor)));

        // Assert
        verify(authorService)
                .findAll(argThat(pageRequest -> Objects.equals(pageRequest, PageRequest.of(0, 2))));
    }

    @Test
    @DisplayName("findAllEndpoint_shouldReturnEmptyPagination")
    void findAllEndpoint_shouldReturnEmptyPagination() throws Exception {
        // Arrange
        final Pagination<Author> emptyPaginatedAuthor = Pagination.empty();

        when(authorService.findAll(PageRequest.of(0, 2)))
                .thenReturn(emptyPaginatedAuthor);

        // Act
        mockMvc.perform(get("/api/rest/authors?page=0&size=2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(emptyPaginatedAuthor)));

        // Assert
        verify(authorService)
                .findAll(argThat(pageRequest -> Objects.equals(pageRequest, PageRequest.of(0, 2))));
    }

    @Test
    @DisplayName("findEndpoint_shouldReturnAuthorWithGivenId")
    void findEndpoint_shouldReturnAuthorWithGivenId() throws Exception {

        // Arrange
        final Date now = new Date();
        final Author savedAuthor = new Author("James");
        savedAuthor.setId(1L);
        savedAuthor.setCreatedAt(String.valueOf(now));
        savedAuthor.setUpdatedAt(String.valueOf(now));

        when(authorService.findById(argThat(id -> Objects.equals(id, 1L))))
                .thenReturn(savedAuthor);

        // Act
        mockMvc.perform(get("/api/rest/authors/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(savedAuthor)));

        // Assert
        verify(authorService)
                .findById(argThat(id -> Objects.equals(id, 1L)));
    }

    @Test
    @DisplayName("findEndpoint_shouldReturn404StatusWhenServiceThrowNotFoundError")
    void findEndpoint_shouldReturn404StatusWhenServiceThrowNotFoundError() throws Exception {

        // Arrange
        when(authorService.findById(argThat(id -> Objects.equals(id, 1L))))
                .thenThrow(new NotFoundException());

        // Act
        mockMvc.perform(get("/api/rest/authors/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        // Assert
        verify(authorService)
                .findById(argThat(id -> Objects.equals(id, 1L)));
    }

    @Test
    @DisplayName("createEndpoint_shouldReturn201Status")
    void createEndpoint_shouldReturn201Status() throws Exception {

        // Arrange
        final Date now = new Date();
        final Author authorAsRequest = new Author("James");
        final Author savedAuthor = new Author("James");
        savedAuthor.setId(1L);
        savedAuthor.setCreatedAt(String.valueOf(now));
        savedAuthor.setUpdatedAt(String.valueOf(now));

        when(authorService.save(authorAsRequest))
                .thenReturn(savedAuthor);

        // Act
        mockMvc.perform(post("/api/rest/authors")
                .content(objectMapper.writeValueAsString(authorAsRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(201))
                .andExpect(content().json(objectMapper.writeValueAsString(savedAuthor)));

        // Assert
        verify(authorService)
                .save(authorAsRequest);
    }

    @Test
    @DisplayName("updateEndpoint_shouldReturn200Status")
    void updateEndpoint_shouldReturn200Status() throws Exception {

        // Arrange
        Long id = 1L;
        final Date now = new Date();
        final Author authorAsRequest = new Author("James Rod");
        final Author authorFromDatabase = new Author("James Rod");
        authorFromDatabase.setId(id);
        authorFromDatabase.setCreatedAt(String.valueOf(now));
        authorFromDatabase.setUpdatedAt(String.valueOf(now));

        when(authorService.update(id, authorAsRequest))
                .thenReturn(authorFromDatabase);

        // Act
        mockMvc.perform(put("/api/rest/authors/1")
                .content(objectMapper.writeValueAsString(authorAsRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(content().json(objectMapper.writeValueAsString(authorFromDatabase)));

        // Assert
        verify(authorService)
                .update(id, authorAsRequest);
    }

    @Test
    @DisplayName("updateEndpoint_shouldReturn404StatusWhenServiceThrowNotFoundError")
    void updateEndpoint_shouldReturn404StatusWhenServiceThrowNotFoundError() throws Exception {

        // Arrange
        Long id = 1L;
        final Author authorAsRequest = new Author("James");

        when(authorService.update(id, authorAsRequest))
                .thenThrow(new NotFoundException());

        // Act
        mockMvc.perform(put("/api/rest/authors/1")
                .content(objectMapper.writeValueAsString(authorAsRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        // Assert
        verify(authorService)
                .update(id, authorAsRequest);
    }

    @Test
    @DisplayName("deleteEndpoint_shouldReturn200Status")
    void deleteEndpoint_shouldReturn200Status() throws Exception {

        // Arrange
        // Act
        mockMvc.perform(delete("/api/rest/authors/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(content().string("Author successfully deleted"));

        // Assert
        verify(authorService)
                .deleteById(argThat(id -> Objects.equals(id, 1L)));
    }

    @Test
    @DisplayName("deleteEndpoint_shouldReturn404StatusWhenServiceThrowNotFoundError")
    void deleteEndpoint_shouldReturn404StatusWhenServiceThrowAnyError() throws Exception {

        // Arrange
        doThrow(new NotFoundException())
                .when(authorService)
                .deleteById(argThat(id -> Objects.equals(id, 1L)));

        // Act
        mockMvc.perform(delete("/api/rest/authors/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        // Assert
        verify(authorService)
                .deleteById(argThat(id -> Objects.equals(id, 1L)));
    }

}
