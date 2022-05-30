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
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("searchEndpoint_shouldReturnPaginatedBookContainingTheSearchKeyOnTitle")
    void searchEndpoint_shouldReturnPaginatedBookContainingTheSearchKeyOnTitle() throws Exception {
        // Arrange
        List<Book> books = Arrays.asList(new Book(), new Book());
        final PageBean pageBean = new PageBean.Builder()
                .page(0)
                .size(10)
                .build();

        final Pagination<Book> paginatedBook = Pagination.of(books, pageBean);

        final PageRequest pageRequest = PageRequest.of(0, 10);

        when(bookService.findAll(BookFilter.of("head"), pageRequest))
                .thenReturn(paginatedBook);

        // Act
        mockMvc.perform(get("/api/rest/books?title=head")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(paginatedBook)));

        // Assert
        verify(bookService)
                .findAll(
                        argThat(filter -> Objects.equals(filter.getSearchKey(), "head")),
                        argThat(toVerifyPageRequest -> Objects.equals(pageRequest, toVerifyPageRequest))
                );
    }

    @Test
    @DisplayName("searchEndpoint_shouldReturnPaginatedBookContainingTheSearchKeyOnTitleWithPagingAndSort")
    void searchEndpoint_shouldReturnPaginatedBookContainingTheSearchKeyOnTitleWithPagingAndSort() throws Exception {
        // Arrange
        List<Book> books = Arrays.asList(new Book());
        final PageBean pageBean = new PageBean.Builder()
                .page(0)
                .size(1)
                .build();

        final Pagination<Book> paginatedBook = Pagination.of(books, pageBean);

        final PageRequest pageRequest = PageRequest.of(0, 1, Sort.by(Sort.Direction.ASC, "title"));

        when(bookService.findAll(BookFilter.of("head"), pageRequest))
                .thenReturn(paginatedBook);

        // Act
        mockMvc.perform(get("/api/rest/books?title=head&page=0&size=1&sort=title,asc")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(paginatedBook)));

        // Assert
        verify(bookService)
                .findAll(
                        argThat(filter -> Objects.equals(filter.getSearchKey(), "head")),
                        argThat(toVerifyPageRequest -> Objects.equals(pageRequest, toVerifyPageRequest))
                );
    }

    @Test
    @DisplayName("searchEndpoint_shouldReturnPaginatedBookContainingTheSearchKeyOnTitleWithInvalidSort")
    void searchEndpoint_shouldReturnPaginatedBookContainingTheSearchKeyOnTitleWithInvalidSort() throws Exception {
        // Arrange
        List<Book> books = Arrays.asList(new Book());
        final PageBean pageBean = new PageBean.Builder()
                .page(0)
                .size(1)
                .build();

        final Pagination<Book> paginatedBook = Pagination.of(books, pageBean);

        final PageRequest pageRequest = PageRequest.of(0, 1, Sort.unsorted());

        when(bookService.findAll(BookFilter.of("head"), pageRequest))
                .thenReturn(paginatedBook);

        // Act
        mockMvc.perform(get("/api/rest/books?title=head&page=0&size=1&sort=asc")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(paginatedBook)));

        // Assert
        verify(bookService)
                .findAll(
                        argThat(filter -> Objects.equals(filter.getSearchKey(), "head")),
                        argThat(toVerifyPageRequest -> Objects.equals(pageRequest, toVerifyPageRequest))
                );
    }
}
