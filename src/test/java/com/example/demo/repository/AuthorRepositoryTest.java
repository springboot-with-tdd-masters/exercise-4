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
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@DataJpaTest
class AuthorRepositoryTest {

    @MockBean
    private AuthorRepository authorRepository;

    private final String author1 = "J. K. Rowling";
    private final String author3 = "William Shakespeare";
    private final String author4 = "Mitch Albom";

    private List<Author> authorList = new ArrayList<>();

    @BeforeEach
    public void setUp(){
        authorList.add(addAuthor(author1, 1L));
        authorList.add(addAuthor(author3, 3L));
        authorList.add(addAuthor(author4, 4L));
    }

    @AfterEach
    public void close(){
        authorList = new ArrayList<>();
    }

    private Author addAuthor(String author, long id) {
        return Author.builder().id(id).name(author).build();
    }

    @Test
    @DisplayName("Should return all authors")
    public void testFindAllAuthors(){
        when(authorRepository.findAll()).thenReturn(authorList);
        List<Author> retrievedAuthors = authorRepository.findAll();
        assertEquals(3, retrievedAuthors.size());
    }

    @Test
    @DisplayName("Should successfully delete by Id")
    public void testFindBookById(){
        long id = 1L;
        when(authorRepository.findById(id)).thenReturn(Optional.ofNullable(authorList.get(0)));
        Author requestedAuthor = authorRepository.findById(id).get();
        assertEquals(author1, requestedAuthor.getName());
        assertEquals(id, requestedAuthor.getId());
    }

    @Test
    @DisplayName("Should delete 1 author based on the requested Id")
    public void testDeleteBookById(){
        long id = 1L;
        authorList.remove(0);
        when(authorRepository.findAll()).thenReturn(authorList);
        doNothing().when(authorRepository).deleteById(id);
        authorRepository.deleteById(id);

        List<Author> retrievedNewSetOfAuthors = authorRepository.findAll();
        assertEquals(2, retrievedNewSetOfAuthors.size());
    }

    @Test
    @DisplayName("Should read author containing the given name")
    public void testFindBookByTitleContainingIgnoreCase(){
        Pageable pageRequest = PageRequest.of(0, 3, Sort.by("id").descending());
        String partOfName = "Mitch";
        List<Author> sortedAuthor = authorList.stream()
                .filter( b -> b.getName().contains(partOfName))
                .sorted(Comparator.comparing(Author::getId).reversed())
                .collect(Collectors.toList());
        Page<Author> authorPage = new PageImpl<>(sortedAuthor);
        when(authorRepository.findByNameContainingIgnoreCase(partOfName, pageRequest)).thenReturn(authorPage);
        Page<Author> requestedAuthor = authorRepository.findByNameContainingIgnoreCase(partOfName, pageRequest);

        assertThat(requestedAuthor.getContent(),hasSize(1));
        assertTrue(requestedAuthor.getContent().get(0).getName().contains(partOfName));
    }
}