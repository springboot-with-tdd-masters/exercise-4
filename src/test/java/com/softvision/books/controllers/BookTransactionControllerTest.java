package com.softvision.books.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softvision.books.services.BookService;
import com.softvision.books.services.domain.Book;
import com.softvision.books.services.domain.BookFilter;
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
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookTransactionController.class)
public class BookTransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean // Meaning replace bookService bean in the container with this mock.
    private BookService bookService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("findAllEndpoint_shouldReturnPaginatedBooks")
    void findAllEndpoint_shouldReturnPaginatedBooks() throws Exception {
        // Arrange
        List<Book> books = Arrays.asList(new Book(), new Book());
        final PageBean pageBean = new PageBean.Builder()
                .page(0)
                .size(2)
                .build();

        final Pagination<Book> paginatedBook = Pagination.of(books, pageBean);

        when(bookService.findAll(BookFilter.of(1L), PageRequest.of(0, 2)))
                .thenReturn(paginatedBook);

        // Act
        final ResultActions perform = mockMvc.perform(get("/api/rest/authors/1/books?page=0&size=2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(paginatedBook)));

        // Assert
        verify(bookService)
                .findAll(
                        argThat(filter -> Objects.equals(filter, BookFilter.of(1L))),
                        argThat(pageRequest -> Objects.equals(pageRequest, PageRequest.of(0, 2)))
                );
    }

    @Test
    @DisplayName("findEndpoint_shouldReturnAuthorWithGivenId")
    void findEndpoint_shouldReturnAuthorWithGivenId() throws Exception {

        // Arrange
        final Date now = new Date();
        Long id = 1L;
        final Book book = new Book("Title", "Description");
        book.setId(id);
        book.setCreatedAt(String.valueOf(now));
        book.setUpdatedAt(String.valueOf(now));

        when(bookService.findById(id))
                .thenReturn(book);

        // Act
        mockMvc.perform(get("/api/rest/authors/1/books/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(book)));

        // Assert
        verify(bookService)
                .findById(id);
    }

    @Test
    @DisplayName("saveEndpoint_shouldReturn201Status")
    void saveEndpoint_shouldReturn201Status() throws Exception {

        // Arrange
        final Book bookAsRequest = new Book("Sample Title", "Sample Author");

        final Book savedBook = new Book(1L, "Sample Title", "Sample Author");
        when(bookService.add(1L, bookAsRequest))
                .thenReturn(savedBook);

        // Act
        mockMvc.perform(post("/api/rest/authors/1/books")
                .content(objectMapper.writeValueAsString(bookAsRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(201))
                .andExpect(content().json(objectMapper.writeValueAsString(savedBook)));

        // Assert
        verify(bookService)
                .add(1L, bookAsRequest);
    }

    @Test
    @DisplayName("saveEndpoint_shouldReturn500StatusWhenServiceThrowAnyError")
    void saveEndpoint_shouldReturn500StatusWhenServiceThrowAnyError() throws Exception {

        // Arrange
        final Book bookAsRequest = new Book("Sample Title", "Sample Author");

        when(bookService.add(1L, bookAsRequest))
                .thenThrow(new RuntimeException());

        // Act
        mockMvc.perform(post("/api/rest/authors/1/books")
                .content(objectMapper.writeValueAsString(bookAsRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError());

        // Assert
        verify(bookService)
                .add(1L, bookAsRequest);
    }

    @Test
    @DisplayName("updateEndpoint_shouldReturn200Status")
    void updateEndpoint_shouldReturn200Status() throws Exception {

        // Arrange
        Long id = 1L;
        final Book bookAsRequest = new Book("Title", "Description");
        final Book bookFromDatabase = new Book("Title", "Description");
        bookFromDatabase.setId(id);

        when(bookService.update(id, bookAsRequest))
                .thenReturn(bookFromDatabase);

        // Act
        mockMvc.perform(put("/api/rest/authors/1/books/1")
                .content(objectMapper.writeValueAsString(bookAsRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(content().json(objectMapper.writeValueAsString(bookFromDatabase)));

        // Assert
        verify(bookService)
                .update(id, bookAsRequest);
    }

    @Test
    @DisplayName("deleteEndpoint_shouldReturn200Status")
    void deleteEndpoint_shouldReturn200Status() throws Exception {

        // Arrange
        // Act
        mockMvc.perform(delete("/api/rest/authors/1/books/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(content().string("Book successfully deleted"));

        // Assert
        verify(bookService)
                .deleteById(argThat(id -> Objects.equals(id, 1L)));
    }

}
