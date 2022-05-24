package com.softvision.library.tdd.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softvision.library.tdd.service.AuthorService;
import com.softvision.library.tdd.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest
@AutoConfigureMockMvc
public abstract class AbstractControllerTests {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    BookService bookService;
    @MockBean
    AuthorService authorService;

    static final ObjectMapper objectMapper = new ObjectMapper();
}
