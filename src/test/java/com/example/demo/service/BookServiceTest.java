package com.example.demo.service;

import com.example.demo.model.Author;
import com.example.demo.model.Book;
import com.example.demo.model.BookRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

@SpringBootTest
class BookServiceTest {

    @MockBean
    private BookService bookService;

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
        when(bookService.createNewBook(bookRequest)).thenReturn(5L);

        bookService.createNewBook(bookRequest);

        ArgumentCaptor<BookRequest> bookRequestCaptor = ArgumentCaptor.forClass(BookRequest.class);
        verify(bookService).createNewBook(bookRequestCaptor.capture());

        assertThat(bookRequestCaptor.getValue().getAuthor(), is(author5));
        assertThat(bookRequestCaptor.getValue().getTitle(), is(bookTitle5));
    }

    @Test
    @DisplayName("Given a successful read by ID, response should give http status 200")
    public void shouldReadById() throws Exception {
        when(bookService.readBookById(1L)).thenReturn(bookList.get(0));
        Book book = bookService.readBookById(1L);
        assertThat(bookTitle1, is(book.getTitle()));
    }

    @Test
    @DisplayName("All book containing the requested string in their title should be returned")
    public void shouldReadBooksContainingTitleWithSortAndPagination() throws Exception {
        Pageable pageRequest = PageRequest.of(0, 3, Sort.by("id").descending());
        List<Book> sortedBooks = bookList.stream()
                .filter( b -> b.getTitle().contains("the"))
                .sorted(Comparator.comparing(Book::getId).reversed())
                .collect(Collectors.toList());
        Page<Book> bookPage = new PageImpl<>(sortedBooks);
        when(bookService.readBookByTitle("the", pageRequest)).thenReturn(bookPage);

        bookService.readBookByTitle("the", pageRequest);

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(bookService).readBookByTitle(eq("the"), pageableCaptor.capture());
        PageRequest pageable = (PageRequest) pageableCaptor.getValue();
        assertThat(pageable.getPageNumber(), is(0));
        assertThat(pageable.getPageSize(), is(3));

        Page<Book> page = bookService.readBookByTitle("the", pageRequest);
        assertThat(page.getContent(),hasSize(3));
    }

    @Test
    @DisplayName("All Books should be returned based on Pageable request")
    public void testReadAllBooks(){
        Pageable pageRequest = PageRequest.of(0, 4, Sort.by("title").ascending());
        List<Book> sortedBooks = bookList.stream().sorted(Comparator.comparing(Book::getTitle)).collect(Collectors.toList());
        Page<Book> bookPage = new PageImpl<>(sortedBooks);
        when(bookService.readAllBooks(pageRequest)).thenReturn(bookPage);

        bookService.readAllBooks(pageRequest);

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(bookService).readAllBooks(pageableCaptor.capture());
        PageRequest pageable = (PageRequest) pageableCaptor.getValue();
        assertThat(pageable.getPageNumber(), is(0));
        assertThat(pageable.getPageSize(), is(4));

        Page<Book> page = bookService.readAllBooks(pageRequest);
        assertThat(page.getContent(),hasSize(4));
    }

    @Test
    @DisplayName("Books should be updated based on ID")
    public void testUpdateBook(){
        String newAuthorName = author1.concat("Perry");
        BookRequest bookRequest = new BookRequest();
        bookRequest.setTitle(bookTitle1);
        bookRequest.setAuthor(newAuthorName);

        Long id = 1L;
        when(bookService.updateBook(id, bookRequest)).thenReturn(updateBook(bookList.get(0), newAuthorName));
        bookService.updateBook(id, bookRequest);

        ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<BookRequest> bookRequestCaptor = ArgumentCaptor.forClass(BookRequest.class);
        verify(bookService).updateBook(idCaptor.capture(), bookRequestCaptor.capture());
        assertThat(idCaptor.getValue(), is(id));
        assertThat(bookRequestCaptor.getValue().getAuthor(), is(newAuthorName));
    }

    private Book updateBook(Book book, String newAuthorName) {
        book.getAuthor().setName(newAuthorName);
        return book;
    }

    @Test
    @DisplayName("Books should be deleted based on ID")
    public void testDeleteBookById(){
        Long id = 1L;
        doNothing().when(bookService).deleteBookById(id);
        bookService.deleteBookById(id);
        ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
        verify(bookService).deleteBookById(idCaptor.capture());
        assertThat(idCaptor.getValue(), is(id));
    }
}