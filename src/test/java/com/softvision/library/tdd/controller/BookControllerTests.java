package com.softvision.library.tdd.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softvision.library.tdd.model.Book;
import com.softvision.library.tdd.model.RecordNotFoundException;
import com.softvision.library.tdd.service.BookService;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;
import static com.softvision.library.tdd.LibraryMocks.*;

import java.util.*;

@AutoConfigureMockMvc
@WebMvcTest(controllers = BookController.class)
public class BookControllerTests {

    @Autowired
    MockMvc mockMvc;
    @Captor
    ArgumentCaptor<Pageable> pageableCaptor;

    @MockBean
    BookService bookService;

    static final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("Given a successful getAll, response should give http status 200 with the list.")
    void test_getAll_success() throws Exception {
        when(bookService.getAll(Pageable.ofSize(2)))
                .thenReturn(createMockPage(List.of(getMockBook1(), getMockBook2())));

        mockMvc.perform(get("/books?size=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title", Matchers.is(MOCK_TITLE_AOW)))
                .andExpect(jsonPath("$.content[1].title", Matchers.is(MOCK_TITLE_TP)));

        verify(bookService, atMostOnce()).getAll(any());
    }

    @Test
    @DisplayName("Given paging and sorting request params, response should be sorted and paged correspondingly.")
    void test_getAll_success_withPagination() throws Exception {

        when(bookService.getAll(pageableCaptor.capture()))
                .thenReturn(createMockPage(List.of(getMockBook1(), getMockBook2())));

        mockMvc.perform(get("/books")
                    .param("page", "1")
                    .param("size", "2")
                    .param("sort", "id,asc")
                    .param("sort", "name,desc"))
                .andExpect(jsonPath("$.content[0].title", Matchers.is(MOCK_TITLE_AOW)))
                .andExpect(jsonPath("$.content[1].title", Matchers.is(MOCK_TITLE_TP)))
                .andExpect(status().isOk());

        PageRequest pageable = (PageRequest) pageableCaptor.getValue();

        Assertions.assertThat(pageable)
                .extracting(AbstractPageRequest::getPageNumber).
                isEqualTo(1);
        Assertions.assertThat(pageable)
                .extracting(AbstractPageRequest::getPageSize)
                .isEqualTo(2);
        Assertions.assertThat(pageable)
                .extracting(AbstractPageRequest::getSort)
                .isEqualTo(Sort.by(
                        Sort.Order.asc("id"),
                        Sort.Order.desc("name")));

        verify(bookService, atMostOnce()).getAll(any());
    }

    @Test
    @DisplayName("Given title as query param, response should only have book name containing 'The'.")
    void test_getAll_success_withPaginationAndTitle() throws Exception {

        when(bookService.getContainingTitle(eq("The"), pageableCaptor.capture()))
                .thenReturn(createMockPage(List.of(getMockBook1(), getMockBook2())));

        mockMvc.perform(get("/books")
                        .param("page", "1")
                        .param("size", "2")
                        .param("title", "The"))
                .andExpect(jsonPath("$.content[0].title", Matchers.is(MOCK_TITLE_AOW)))
                .andExpect(jsonPath("$.content[1].title", Matchers.is(MOCK_TITLE_TP)))
                .andExpect(status().isOk());

        PageRequest pageable = (PageRequest) pageableCaptor.getValue();

        Assertions.assertThat(pageable)
                .extracting(AbstractPageRequest::getPageNumber).
                isEqualTo(1);
        Assertions.assertThat(pageable)
                .extracting(AbstractPageRequest::getPageSize)
                .isEqualTo(2);

        verify(bookService, atMostOnce()).getContainingTitle(any(), any());
    }

    @Test
    @DisplayName("Given a record not found from service getAll, response should give http status 404 (not found).")
    void test_getAll_fail() throws Exception {
        when(bookService.getAll(any())).thenThrow(RecordNotFoundException.class);

        mockMvc.perform(get("/books")).andExpect(status().isNotFound());

        verify(bookService, atMostOnce()).getAll(any());
    }

    @Test
    @DisplayName("Given a successful result from createOrUpdate, response should give http status 201 (created).")
    void test_create() throws Exception {
        Book book = getMockBook1();
        when(bookService.createOrUpdate(argThat(b -> b.getTitle().equals(MOCK_TITLE_AOW))))
                .thenReturn(book);

        mockMvc.perform(post("/books").content(objectMapper.writeValueAsBytes(book))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("title", Matchers.is(MOCK_TITLE_AOW)));

        verify(bookService, atMostOnce()).createOrUpdate(any());
    }

    @Test
    @DisplayName("Given a failure result from createOrUpdate, response should give http status 5xx (server error).")
    void test_create_fail() throws Exception {
        Book book = getMockBook1();
        when(bookService.createOrUpdate(argThat(b -> b.getTitle().equals(MOCK_TITLE_AOW))))
                .thenThrow(new IllegalArgumentException());

        mockMvc.perform(post("/books").content(objectMapper.writeValueAsBytes(book))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError());

        verify(bookService, atMostOnce()).createOrUpdate(any());
    }

    private Page<Book> createMockPage(List<Book> content) {
        return new PageImpl<>(content);
    }
}
