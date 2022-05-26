package com.example.exercise2.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.exercise2.service.BookService;
import com.example.exercise2.service.model.Author;
import com.example.exercise2.service.model.Book;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(BookController.class)
@AutoConfigureMockMvc
public class BookControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private BookService bookService;

  private final ObjectMapper objectMapper = new ObjectMapper();


  @Test
  @DisplayName("Should return list of book with title containing passed string")
  void should_return_list_of_books_containing_title_string() throws Exception {
    //GET - /books?title=Bl

    Author author1 = new Author(1l, "Mr Bibo");
    List<Book> bookList =
        Arrays.asList(
            new Book(1L, "Black Book", "Librong Itom1", author1),
            new Book(2L, "Blue Book", "Librong Itom2", author1),
            new Book(3L, "Brown Book", "Librong Itom3", author1),
            new Book(4L, "Beige Book", "Librong Itom4", author1),
            new Book(5L, "White Book", "Librong Itom5", author1),
            new Book(6L, "Red Book", "Librong Itom6", author1));

    String[] sort = {"id", "desc"};
    String titleStr = "Bl";

    Pageable pagingSort = PageRequest.of(0, 3, Sort.by("id").descending());
    Page<Book> expectedResponse = new PageImpl<>(bookList, pagingSort, bookList.size());

    when(bookService.findBooksByTitleContaining(titleStr, 0, 3, sort)).thenReturn(expectedResponse);

    this.mockMvc.perform(get("/books?title=Bl")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.totalPages").value(2))
        .andExpect(MockMvcResultMatchers.jsonPath("$.numberOfElements").value(6))
        .andExpect(MockMvcResultMatchers.jsonPath("$.size").value(3))

    ;
    verify(bookService).findBooksByTitleContaining(titleStr, 0, 3, sort);
  }
}
