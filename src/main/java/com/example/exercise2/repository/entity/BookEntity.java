package com.example.exercise2.repository.entity;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class BookEntity extends BaseEntity {


  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", nullable = false)
  private Long id;
  private String title;
  private String description;

  @ManyToOne
  @JoinColumn(name = "author_id")
  private AuthorEntity author;

  public BookEntity() {
  }

  public BookEntity(Long id, String title, String description, AuthorEntity author) {
    this.id = id;
    this.title = title;
    this.description = description;
    this.author = author;
  }

  public BookEntity(String title, String description, AuthorEntity author) {
    this.title = title;
    this.description = description;
    this.author = author;
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

  public AuthorEntity getAuthor() {
    return author;
  }

  public void setAuthor(AuthorEntity author) {
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
    BookEntity that = (BookEntity) o;
    return Objects.equals(id, that.id) && Objects.equals(title, that.title)
        && Objects.equals(description, that.description) && Objects.equals(author,
        that.author);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, title, description, author);
  }
}
