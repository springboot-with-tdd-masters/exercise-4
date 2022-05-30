package com.softvision.books.services.converters.impl;

import com.softvision.books.repositories.entities.BookEntity;
import com.softvision.books.services.converters.AuthorConverter;
import com.softvision.books.services.converters.BookConverter;
import com.softvision.books.services.domain.Book;
import com.softvision.books.services.domain.PageBean;
import com.softvision.books.services.domain.Pagination;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class BookConverterImpl implements BookConverter {

    private AuthorConverter authorConverter;

    public BookConverterImpl(AuthorConverter authorConverter) {
        this.authorConverter = authorConverter;
    }

    @Override
    public BookEntity convert(Book book) {

        final BookEntity bookEntity = new BookEntity();

        bookEntity.setId(book.getId());
        bookEntity.setTitle(book.getTitle());
        bookEntity.setDescription(book.getDescription());
        if (Objects.nonNull(book.getAuthor())) {
            bookEntity.setAuthor(authorConverter.convert(book.getAuthor()));
        }

        return bookEntity;
    }

    @Override
    public Book convert(BookEntity bookEntity) {

        final Book book = new Book();

        book.setId(bookEntity.getId());
        book.setTitle(bookEntity.getTitle());
        book.setDescription(bookEntity.getDescription());
        book.setAuthor(authorConverter.convert(bookEntity.getAuthor()));
        book.setCreatedAt(String.valueOf(bookEntity.getCreatedDate()));
        book.setUpdatedAt(String.valueOf(bookEntity.getLastModifiedDate()));

        return book;
    }

    @Override
    public Pagination<Book> convert(Page<BookEntity> page) {

        final List<Book> books = page.get()
                .map(this::convert)
                .collect(Collectors.toList());

        final Pageable pageable = page.getPageable();

        final PageBean pageBean = new PageBean.Builder()
                .page(pageable.getPageNumber())
                .maxPage(page.getTotalPages())
                .size(pageable.getPageSize())
                .total(page.getTotalElements())
                .build();

        return Pagination.of(books, pageBean);
    }
}
