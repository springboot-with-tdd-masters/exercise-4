package com.example.demo;

import com.example.demo.model.Author;
import com.example.demo.model.Book;
//import com.example.demo.repository.AuthorRepository;
import com.example.demo.repository.AuthorRepository;
import com.example.demo.repository.BookRepository;
import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@EnableJpaAuditing(auditorAwareRef = "auditor")
public class BookInitializer implements CommandLineRunner {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;


    @Override
    public void run(String... args) throws Exception {
        log.info("Starting to initialize sample data ...");

        Faker faker = new Faker();
        for (int i = 0; i < 3; i++) {
            Author author = buildAuthor(faker.book().author());
            for (int j = 0; j < 3; j++) {
                Book book = new Book();
                book.setTitle(faker.book().title());
                book.setAuthor(author);
                bookRepository.save(book);
            }
        }
        log.info("... finished with data initialization");
    }

    private Author buildAuthor(String fakeAuthor) {
        Author author = new Author();
        author.setName(fakeAuthor);
        return authorRepository.save(author);
    }
}
