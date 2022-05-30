package com.example.bookTDD.controller;

import com.example.bookTDD.model.Author;
import com.example.bookTDD.model.Book;
import com.example.bookTDD.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@WebMvcTest
@AutoConfigureMockMvc
public class BookTestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    ObjectMapper mapper = new ObjectMapper();

    @MockBean
    private BookController bookController;

    @MockBean
    private AuthorController authorController;

    @Autowired
    BookService bookService;


    @Test
    @DisplayName("Get all books, response should give http status 200")
    public void shouldReturnAllBooks() throws Exception {

        Author author = new Author();
        author.setId(1L);
        author.setName("Zaldy");

        Book book = new Book();
        book.setTitle("JK Test");
        book.setAuthor(author);
        book.setId(1L);
        book.setDescription("Test");

        when(bookController.createBook(1L, book)).thenReturn(book);

        mockMvc.perform(post("/authors/{authorId}/books")
                        .content(mapper.writeValueAsBytes(book))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("title", Matchers.is("JK Test")))
                .andExpect(jsonPath("author", Matchers.is("Zaldy")));

    }

}
