package com.softvision.books.services.converters;

import com.softvision.books.repositories.entities.BookEntity;
import com.softvision.books.services.domain.Book;
import com.softvision.books.services.domain.Pagination;
import org.springframework.data.domain.Page;

public interface BookConverter {

    BookEntity convert(Book book);

    Book convert(BookEntity bookEntity);

    Pagination<Book> convert(Page<BookEntity> page);
}
