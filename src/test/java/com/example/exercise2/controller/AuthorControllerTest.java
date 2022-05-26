package com.example.exercise2.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.exercise2.exception.AuthorNotFoundException;
import com.example.exercise2.service.AuthorService;
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

@WebMvcTest(AuthorController.class)
@AutoConfigureMockMvc
public class AuthorControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private AuthorService authorService;

  @MockBean
  private BookService bookService;

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Test
  @DisplayName("Given a successful save of author, should return author details and http status 200")
  void save_and_return_author_details_with_http_200() throws Exception {

    Author requestAuthor = new Author(1l, "Mr Bibo");

    Author expectedAuthor = new Author(1l, "Mr Bibo");

    when(authorService.save(requestAuthor.getName())).thenReturn(expectedAuthor);

    this.mockMvc.perform(post("/authors").content(
            objectMapper.writeValueAsString(requestAuthor)
        ).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().json(objectMapper.writeValueAsString(expectedAuthor)));

    verify(authorService).save(requestAuthor.getName());
  }

  @Test
  @DisplayName("Get list of authors with paged 0, size 2 and sorted by name, ascending")
  void get_authorlist_with_paged0_size2_sortedbyNameAsc() throws Exception {
  //GET - /authors?page=0&size=2&sort=name,asc

    List<Author> authorList =
        Arrays.asList(new Author(1l, "Mr Bibo"),
            new Author(2l, "Mr Bibo2"),
            new Author(3l, "Mr Bibo3"),
            new Author(4l, "Mr Bibo4"),
            new Author(5l, "Mr Bibo5"),
            new Author(6l, "Mr Bibo6"))
    ;

    String[] sort = {"name","asc"};

    Pageable pagingSort = PageRequest.of(0, 2, Sort.by("name").descending());
    Page<Author> expectedResponse = new PageImpl<>(authorList,pagingSort,authorList.size());

    when(authorService.findAll(0,2,sort)).thenReturn(expectedResponse);

    this.mockMvc.perform(get("/authors?page=0&size=2&sort=name,asc")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.totalPages").value(3))
        .andExpect(MockMvcResultMatchers.jsonPath("$.numberOfElements").value(6))
        .andExpect(MockMvcResultMatchers.jsonPath("$.size").value(2))
    ;

    verify(authorService).findAll(0,2,sort);

  }

  @Test
  @DisplayName("Should return author details given a valid author id")
  void should_return_singleAuthorDetails() throws Exception {
  //GET - /authors/{id}
    Author expectedResponse = new Author(2L, "Mr Bibo2");
    when(authorService.findById(2)).thenReturn(expectedResponse);

    this.mockMvc.perform(get("/authors/2")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Mr Bibo2"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.id").value("2"))
    ;
    verify(authorService).findById(2);
  }

  @Test
  @DisplayName("Should return exception given invalid author id")
  void should_return_NoDataFoundException() throws Exception {

    when(authorService.findById(2)).thenThrow(new AuthorNotFoundException());

    this.mockMvc.perform(get("/authors/2")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
    ;
    verify(authorService).findById(2);
  }


  @Test
  @DisplayName("Given a successful save of book, should return book details and http status 200")
  void save_and_return_book_details_with_http_200() throws Exception {

    Author author = new Author(1l, "Mr Bibo");
    Book expectedBook = new Book();
    expectedBook.setTitle("Blue book");
    expectedBook.setDescription("Blue colored book");
    expectedBook.setAuthor(author);

    when(authorService.findById(1)).thenReturn(author);

    when(bookService.save(any())).thenReturn(expectedBook);

    this.mockMvc.perform(post("/authors/1/books").content(
            objectMapper.writeValueAsString(expectedBook)
        ).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(expectedBook.getTitle()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(expectedBook.getDescription()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.author.id").value(expectedBook.getAuthor().getId()))
    ;
    verify(authorService).findById(1);
    verify(bookService).save(expectedBook);
  }

  @Test
  @DisplayName("Get list of books with paged 0, size 2 and sorted by title, ascending")
  void get_booklist_with_paged0_size2_sortedbyTitleAsc() throws Exception {
    //GET - /authors/{id}/books?page=0&size=2&sort=name,asc
  }

  @Test
  @DisplayName("Should return book details given a valid book id")
  void should_return_singleBookDetails() throws Exception {
    //GET - /authors/{authorid}/books/{bookid}

    Author expectedAuthor = new Author(2L, "Mr Bibo2");
    Book expectedBook = new Book(1L,"Book Title A", "Book Desc C",expectedAuthor);

    when(authorService.findById(2)).thenReturn(expectedAuthor);
    when(bookService.findById(1L)).thenReturn(expectedBook);

    this.mockMvc.perform(get("/authors/2/books/1")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(expectedBook.getTitle()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(expectedBook.getId()))
    ;
    verify(authorService).findById(2);
    verify(bookService).findById(1L);
  }







}
