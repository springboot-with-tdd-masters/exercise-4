package com.example.demo.controller;

import com.example.demo.exception.BookNotFoundException;
import com.example.demo.model.Author;
import com.example.demo.model.Book;
import com.example.demo.model.BookRequest;
import com.example.demo.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
class BookControllerTest {

    private final String bookTitle1 = "Harry Potter and the Sorcerer's Stone";
    private final String bookTitle2 = "Harry Potter and the Chamber of Secrets";
    private final String bookTitle3 = "Othello";
    private final String bookTitle4 = "For One More Day";
    private final String bookTitle5 = "The Green Mile";
    private final String author1 = "J. K. Rowling";
    private final String author3 = "William Shakespeare";
    private final String author4 = "Mitch Albom";
    private final String author5 = "Stephen King";

    private List<Book> bookList = new ArrayList<>();
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookService bookService;

    @Captor
    private ArgumentCaptor<BookRequest> bookRequestArgumentCaptor;

    @BeforeEach
    public void setUp(){
        bookList.add(addBook(bookTitle1, author1, 1L));
        bookList.add(addBook(bookTitle2, author1, 2L));
        bookList.add(addBook(bookTitle3, author3, 3L));
        bookList.add(addBook(bookTitle4, author4, 4L));
    }

    @AfterEach
    public void close(){
        bookList = new ArrayList<>();
    }

    private Book addBook(String title, String author, long id) {
        return Book.builder().id(id).title(title).author(Author.builder().id(id == 2L ? 1L : id).name(author).build()).build();
    }

    @Test
    @DisplayName("Given a successful save, response should give http status 200")
    public void successCreateBook() throws Exception {
        BookRequest bookRequest = new BookRequest();
        bookRequest.setAuthor(author5);
        bookRequest.setTitle(bookTitle5);

        when(bookService.createNewBook(bookRequestArgumentCaptor.capture())).thenReturn(1L);

        this.mockMvc
                .perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookRequest)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(header().string("Location", "http://localhost/api/books/1"));

        assertThat(bookRequestArgumentCaptor.getValue().getAuthor(), is(author5));
        assertThat(bookRequestArgumentCaptor.getValue().getTitle(), is(bookTitle5));
    }

    @Test
    @DisplayName("Given a successful read by ID, response should give http status 200")
    public void shouldReadBookByIdSuccess() throws Exception {
        when(bookService.readBookById(1L)).thenReturn(
                createBook(1L, bookTitle1, author1));

        this.mockMvc
                .perform(get("/api/books/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.title", is(bookTitle1)));
    }

    @Test
    @DisplayName("Given a successful read, response should give http status 200")
    public void successReadAllBooksWithSortAndPagination() throws Exception {
        Pageable pageRequest = PageRequest.of(0, 4, Sort.by("title").ascending());
        List<Book> sortedBooks = bookList.stream().sorted(Comparator.comparing(Book::getTitle)).collect(Collectors.toList());
        Page<Book> bookPage = new PageImpl<>(sortedBooks);
        when(bookService.readAllBooks(pageRequest)).thenReturn(bookPage);

        mockMvc.perform(get("/api/books")
                        .param("page", "0")
                        .param("size", "4")
                        .param("sort", "title,asc")) // <-- no space after comma!!!
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.[0].title", is(bookTitle4)))
                .andExpect(jsonPath("$.content.[2].title", is(bookTitle1)));

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(bookService).readAllBooks(pageableCaptor.capture());
        PageRequest pageable = (PageRequest) pageableCaptor.getValue();
        assertThat(pageable.getPageNumber(), is(0));
        assertThat(pageable.getPageSize(), is(4));

        Page<Book> page = bookService.readAllBooks(pageRequest);
        assertThat(page.getContent(),hasSize(4));
    }

    @Test
    @DisplayName("Given a successful read, response should give http status 200")
    public void successReadAllBooksUsingPageableDefault() throws Exception {
        List<Book> sortedBooks = bookList.stream().sorted(Comparator.comparing(Book::getId)).collect(Collectors.toList());
        Page<Book> bookPage = new PageImpl<>(sortedBooks);
        when(bookService.readAllBooks(null)).thenReturn(bookPage);

        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk());

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(bookService).readAllBooks(pageableCaptor.capture());
        PageRequest pageable = (PageRequest) pageableCaptor.getValue();
        assertThat(pageable.getPageNumber(), is(0));
        assertThat(pageable.getPageSize(), is(10));

        Page<Book> page = bookService.readAllBooks(null);
        assertThat(page.getContent(),hasSize(4));
    }

    @Test
    @DisplayName("Given a successful read by title, response should give http status 200")
    public void successReadBooksContainingTitleWithSortAndPagination() throws Exception {
        Pageable pageRequest = PageRequest.of(0, 3, Sort.by("id").descending());
        List<Book> sortedBooks = bookList.stream()
                .filter( b -> b.getTitle().contains("the"))
                .sorted(Comparator.comparing(Book::getId).reversed())
                .collect(Collectors.toList());
        Page<Book> bookPage = new PageImpl<>(sortedBooks);
        when(bookService.readBookByTitle("the", pageRequest)).thenReturn(bookPage);

        mockMvc.perform(get("/api/books/readByTitle")
                        .param("title", "the")
                        .param("page", "0")
                        .param("size", "3")
                        .param("sort", "id,desc")) // <-- no space after comma!!!
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.[0].title", is(bookTitle3)))
                .andExpect(jsonPath("$.content.[1].title", is(bookTitle2)))
                .andExpect(jsonPath("$.content.[2].title", is(bookTitle1)));

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(bookService).readBookByTitle(eq("the"), pageableCaptor.capture());
        PageRequest pageable = (PageRequest) pageableCaptor.getValue();
        assertThat(pageable.getPageNumber(), is(0));
        assertThat(pageable.getPageSize(), is(3));

        Page<Book> page = bookService.readBookByTitle("the", pageRequest);
        assertThat(page.getContent(),hasSize(3));
    }

    @Test
    @DisplayName("Given a successful read by update, response should give http status 200")
    public void shouldUpdateBookSuccess() throws Exception {
        BookRequest bookRequest = new BookRequest();
        String newAuthorName = author1.concat(" Perry");
        bookRequest.setAuthor(newAuthorName);
        bookRequest.setTitle(bookTitle1);

        when(bookService.updateBook(eq(1L), bookRequestArgumentCaptor.capture()))
                .thenReturn(createBook(1L, bookTitle1, newAuthorName));

        this.mockMvc
                .perform(put("/api/books/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.author.name", is(newAuthorName)))
                .andExpect(jsonPath("$.title", is(bookTitle1)));

        assertThat(bookRequestArgumentCaptor.getValue().getAuthor(), is(newAuthorName));
        assertThat(bookRequestArgumentCaptor.getValue().getTitle(), is(bookTitle1));
    }

    @Test
    @DisplayName("Given a successful delete, response should give http status 200")
    public void successDeleteBook() throws Exception {
        BookRequest bookRequest = new BookRequest();
        bookRequest.setAuthor(author5);
        bookRequest.setTitle(bookTitle5);

        doNothing().when(bookService).deleteBookById(1L);

        this.mockMvc
                .perform(delete("/api/books/1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Given a fail read, response should give http status 404")
    public void shouldReturn404BookNotFoundGet() throws Exception {
        Long bookId = 42L;
        when(bookService.readBookById(bookId)).thenThrow(new BookNotFoundException(String.format("Book with id %s not found", bookId)));
        this.mockMvc
                .perform(get("/api/books/42"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Given a fail read, update is not possible and response should give http status 404")
    public void shouldReturn404BookNotFoundUpdate() throws Exception {
        BookRequest bookRequest = new BookRequest();
        bookRequest.setAuthor(author1);
        bookRequest.setTitle(bookTitle1);

        Long bookId = 42L;
        when(bookService.updateBook(eq(bookId), bookRequestArgumentCaptor.capture()))
                .thenThrow(new BookNotFoundException(String.format("Book with id %s not found", bookId)));

        this.mockMvc
                .perform(put("/api/books/42")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookRequest)))
                .andExpect(status().isNotFound());
    }

    private Book createBook(Long id, String title, String author) {
        Book book = new Book();
        book.setId(id);
        book.setTitle(title);
        book.setAuthor(Author.builder().name(author).build());
        return book;
    }

}