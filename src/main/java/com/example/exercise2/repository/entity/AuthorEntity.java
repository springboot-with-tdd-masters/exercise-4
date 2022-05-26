package com.example.exercise2.repository.entity;

import java.util.Date;
import java.util.Objects;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class AuthorEntity extends BaseEntity {


  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", nullable = false)
  private Long id;
  private String name;

  @OneToMany(cascade = CascadeType.ALL)
  private Set<BookEntity> books;

  public AuthorEntity() {
  }

  public AuthorEntity(Long id, String name,
      Set<BookEntity> books) {
    this.id = id;
    this.name = name;
    this.books = books;
  }

  public AuthorEntity(Long id, String name) {
    this.id = id;
    this.name = name;
  }

  public AuthorEntity(String name) {
    this.name = name;
  }

  public AuthorEntity(Long id, String name, Date createdDate, Date lastModifiedDate) {
    super();
    this.id = id;
    this.name = name;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Set<BookEntity> getBooks() {
    return books;
  }

  public void setBooks(Set<BookEntity> books) {
    this.books = books;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AuthorEntity that = (AuthorEntity) o;
    return Objects.equals(id, that.id) && Objects.equals(name, that.name)
        && Objects.equals(books, that.books);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, books);
  }

  @Override
  public String toString() {
    return "AuthorEntity{" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", books=" + books +
        '}';
  }
}
