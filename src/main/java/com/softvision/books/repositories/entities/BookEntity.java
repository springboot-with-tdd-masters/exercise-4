package com.softvision.books.repositories.entities;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class BookEntity extends AbstractEntity {

    private String title;

    private String description;

    @ManyToOne(optional = false)
    private AuthorEntity author;

    public BookEntity() {}

    public BookEntity(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public AuthorEntity getAuthor() {
        return author;
    }

    public void setAuthor(AuthorEntity author) {
        this.author = author;
    }
}
