package com.example.exercise4.controller;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.exercise4.repository.UserRepository;
import com.example.exercise4.repository.entity.RoleEntity;
import com.example.exercise4.repository.entity.RoleEnum;
import com.example.exercise4.repository.entity.UserEntity;
import com.example.exercise4.service.BookService;
import com.example.exercise4.service.model.Author;
import com.example.exercise4.service.model.Book;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
public class BookControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private BookService bookService;

  @MockBean
  private UserRepository userRepository;

  @MockBean
  private PasswordEncoder passwordEncoder;

  private final ObjectMapper objectMapper = new ObjectMapper();

  private static final UserEntity user = getMockUser("dummyUser");

  private static UserEntity getMockUser(String username) {
    UserEntity user = new UserEntity();
    user.setId(1L);
    user.setUsername(username);
    user.setEmail(username);
    user.setPassword("secret");
    RoleEntity role = new RoleEntity();
    role.setName(RoleEnum.ROLE_USER);
    user.setRoles(Set.of(role));
    return user;
  }
  @BeforeEach
  public void init() {
    SecurityContextHolder.getContext().setAuthentication(
        new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
    when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
    when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
  }

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
