package com.example.exercise2.service.model;

import java.util.Date;
import java.util.Objects;

public class Book extends BaseModel {

  private Long id;
  private String title;
  private String description;
  private Author author;

  public Book(String title, String description, Author author) {
    this.title = title;
    this.description = description;
    this.author = author;
  }

  public Book(Long id, String title, String description, Author author,Date createdDate, Date lastModifiedDate) {
    super(createdDate, lastModifiedDate);
    this.id = id;
    this.title = title;
    this.description = description;
    this.author = author;
  }

  public Book(Long id, String title, String description, Author author) {
    this.id = id;
    this.title = title;
    this.description = description;
    this.author = author;
  }

  public Book() {
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }


  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Author getAuthor() {
    return author;
  }

  public void setAuthor(Author author) {
    this.author = author;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Book book = (Book) o;
    return Objects.equals(id, book.id) && Objects.equals(title, book.title)
        && Objects.equals(description, book.description) && Objects.equals(author,
        book.author);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, title, description, author);
  }
}
