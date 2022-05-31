package com.example.demo.repository;

import com.example.demo.model.Author;
import com.example.demo.model.Book;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;

import java.util.*;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@DataJpaTest
class BookRepositoryTest {

    @MockBean
    private BookRepository bookRepository;

    private final String bookTitle1 = "Harry Potter and the Sorcerer's Stone";
    private final String bookTitle2 = "Harry Potter and the Chamber of Secrets";
    private final String bookTitle3 = "Othello";
    private final String bookTitle4 = "For One More Day";
    private final String author1 = "J. K. Rowling";
    private final String author3 = "William Shakespeare";
    private final String author4 = "Mitch Albom";

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
    @DisplayName("Should return all books")
    public void testFindAllBooks(){
        when(bookRepository.findAll()).thenReturn(bookList);
        List<Book> retrievedBooks = bookRepository.findAll();
        assertEquals(4, retrievedBooks.size());
    }

    @Test
    @DisplayName("Should successfully find by author name")
    public void testFindBookByAuthorName(){
        when(bookRepository.findByAuthorName(author1)).thenReturn(Arrays.asList(bookList.get(0)));
        List<Book> retrievedBooks = bookRepository.findByAuthorName(author1);
        assertEquals(author1, retrievedBooks.get(0).getAuthor().getName());
    }

    @Test
    @DisplayName("Should successfully delete by Id")
    public void testFindBookById(){
        long id = 1L;
        when(bookRepository.findById(id)).thenReturn(Optional.ofNullable(bookList.get(0)));
        Book requestedBook = bookRepository.findById(id).get();
        assertEquals(bookTitle1, requestedBook.getTitle());
        assertEquals(id, requestedBook.getId());
    }

    @Test
    @DisplayName("Should delete 1 book based on the requested Id")
    public void testDeleteBookById(){
        long id = 1L;
        bookList.remove(0);
        when(bookRepository.findAll()).thenReturn(bookList);
        doNothing().when(bookRepository).deleteById(id);
        bookRepository.deleteById(id);

        List<Book> retrievedNewSetOfBooks = bookRepository.findAll();
        assertEquals(3, retrievedNewSetOfBooks.size());
    }

    @Test
    @DisplayName("Should read books containing the given title name")
    public void testFindBookByTitleContainingIgnoreCase(){
        Pageable pageRequest = PageRequest.of(0, 3, Sort.by("id").descending());
        String partOfTitle = "the";
        List<Book> sortedBooks = bookList.stream()
                .filter( b -> b.getTitle().contains(partOfTitle))
                .sorted(Comparator.comparing(Book::getId).reversed())
                .collect(Collectors.toList());
        Page<Book> bookPage = new PageImpl<>(sortedBooks);
        when(bookRepository.findByTitleContainingIgnoreCase(partOfTitle, pageRequest)).thenReturn(bookPage);
        Page<Book> requestedBooks = bookRepository.findByTitleContainingIgnoreCase(partOfTitle, pageRequest);

        assertThat(requestedBooks.getContent(),hasSize(3));
        assertTrue(requestedBooks.getContent().get(0).getTitle().contains(partOfTitle));
    }
}