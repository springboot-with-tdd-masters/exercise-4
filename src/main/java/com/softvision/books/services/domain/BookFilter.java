package com.softvision.books.services.domain;

import java.util.Objects;

public class BookFilter {

    private String searchKey;

    private Long authorId;

    public BookFilter() {}

    public BookFilter(String searchKey, Long authorId) {
        this.searchKey = searchKey;
        this.authorId = authorId;
    }

    public boolean hasSearchKey() {
        return Objects.nonNull(searchKey)
                && !searchKey.trim().isEmpty();
    }

    public String getSearchKey() {
        return searchKey;
    }

    public boolean hasAuthor() {
        return Objects.nonNull(authorId);
    }

    public Long getAuthorId() {
        return authorId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookFilter that = (BookFilter) o;
        return Objects.equals(searchKey, that.searchKey) &&
                Objects.equals(authorId, that.authorId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(searchKey, authorId);
    }

    public static BookFilter of(String searchKey) {
        return new BookFilter(searchKey, null);
    }

    public static BookFilter of(Long authorId) {
        return new BookFilter(null, authorId);
    }

    public static BookFilter empty() {
        return new BookFilter();
    }
}
