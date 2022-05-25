package com.softvision.library.tdd.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softvision.library.tdd.SecurityTestConfig;
import com.softvision.library.tdd.model.Author;
import com.softvision.library.tdd.model.exception.RecordNotFoundException;
import com.softvision.library.tdd.service.AuthorService;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.softvision.library.tdd.LibraryMocks.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest(classes = SecurityTestConfig.class)
public class AuthorControllerTests {

    @Autowired
    MockMvc mockMvc;
    @Captor
    ArgumentCaptor<Pageable> pageableCaptor;

    @MockBean
    AuthorService authorService;

    static final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("Given a successful getAll, response should give HTTP status 200 with the list.")
    @WithUserDetails(MOCK_USER1_USERNAME)
    void test_getAll_success() throws Exception {
        when(authorService.getAll(Pageable.ofSize(2)))
                .thenReturn(createMockPage(List.of(getMockAuthor1(), getMockAuthor2())));

        mockMvc.perform(get("/authors?size=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name", Matchers.is(MOCK_AUTHOR_ST)))
                .andExpect(jsonPath("$.content[1].name", Matchers.is(MOCK_AUTHOR_NM)));

        verify(authorService, atMostOnce()).getAll(any());
    }

    @Test
    @DisplayName("Given paging and sorting request params, response should be sorted and paged correspondingly.")
    @WithUserDetails(MOCK_USER1_USERNAME)
    void test_getAll_success_withPagination() throws Exception {

        when(authorService.getAll(pageableCaptor.capture()))
                .thenReturn(createMockPage(List.of(getMockAuthor1(), getMockAuthor2())));

        mockMvc.perform(get("/authors")
                        .param("page", "1")
                        .param("size", "2")
                        .param("sort", "id,asc")
                        .param("sort", "name,desc"))
                .andExpect(jsonPath("$.content[0].name", Matchers.is(MOCK_AUTHOR_ST)))
                .andExpect(jsonPath("$.content[1].name", Matchers.is(MOCK_AUTHOR_NM)))
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

        verify(authorService, atMostOnce()).getAll(any());
    }

    @Test
    @DisplayName("Given name as query param, response should only have authors name containing 'Sun'.")
    @WithUserDetails(MOCK_USER1_USERNAME)
    void test_getAll_success_withPaginationAndTitle() throws Exception {

        when(authorService.getContainingName(eq("Sun"), pageableCaptor.capture()))
                .thenReturn(createMockPage(List.of(getMockAuthor1(), getMockAuthor2())));

        mockMvc.perform(get("/authors")
                        .param("page", "1")
                        .param("size", "2")
                        .param("name", "Sun"))
                .andExpect(jsonPath("$.content[0].name", Matchers.is(MOCK_AUTHOR_ST)))
                .andExpect(status().isOk());

        PageRequest pageable = (PageRequest) pageableCaptor.getValue();

        Assertions.assertThat(pageable)
                .extracting(AbstractPageRequest::getPageNumber).
                isEqualTo(1);
        Assertions.assertThat(pageable)
                .extracting(AbstractPageRequest::getPageSize)
                .isEqualTo(2);

        verify(authorService, atMostOnce()).getContainingName(any(), any());
    }

    @Test
    @DisplayName("Given a successful result from createOrUpdate, response should give http status 201 (created).")
    @WithUserDetails(MOCK_USER1_USERNAME)
    void test_create() throws Exception {
        Author author = getMockAuthor1();
        when(authorService.create(argThat(a -> a.getName().equals(MOCK_AUTHOR_ST))))
                .thenReturn(author);

        mockMvc.perform(post("/authors").content(objectMapper.writeValueAsBytes(author))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("name", Matchers.is(MOCK_AUTHOR_ST)));

        verify(authorService, atMostOnce()).create(any());
    }

    @Test
    @DisplayName("Given a failure result from createOrUpdate, response should give http status 5xx (server error).")
    @WithUserDetails(MOCK_USER1_USERNAME)
    void test_create_fail() throws Exception {
        Author author = getMockAuthor1();
        when(authorService.create(argThat(a -> a.getName().equals(MOCK_AUTHOR_ST))))
                .thenThrow(IllegalArgumentException.class);

        mockMvc.perform(post("/authors").content(objectMapper.writeValueAsBytes(author))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError());

        verify(authorService, atMostOnce()).create(any());
    }

    @Test
    @DisplayName("Given a record not found from service getAll, response should give http status 404 (not found).")
    @WithUserDetails(MOCK_USER1_USERNAME)
    void test_getAll_fail_notFound() throws Exception {
        when(authorService.getAll(any())).thenThrow(RecordNotFoundException.class);

        mockMvc.perform(get("/authors")).andExpect(status().isNotFound());

        verify(authorService, atMostOnce()).getAll(any());
    }

    @Test
    @DisplayName("Given an anonymous user, response should give http status 403 (forbidden).")
    @WithAnonymousUser()
    void test_getAll_fail_unauthorized() throws Exception {
        mockMvc.perform(get("/authors")).andExpect(status().isForbidden());
    }

    private Page<Author> createMockPage(List<Author> content) {
        return new PageImpl<>(content);
    }
}
