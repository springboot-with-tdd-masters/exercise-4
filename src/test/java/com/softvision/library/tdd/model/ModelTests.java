package com.softvision.library.tdd.model;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

@ExtendWith(MockitoExtension.class)
public class ModelTests {
    @Test
    @DisplayName("Book and Author - should have separate entities")
    void test_book_author_separateEntities(){
        try {
            Book book = new Book();
            Author author = new Author();
            book.setAuthor(author);

            Set<Book> books = Set.of(book);
            author.setBooks(books);
        } catch (Exception e) {
            Assertions.fail("Field should exist");
        }
    }
}
