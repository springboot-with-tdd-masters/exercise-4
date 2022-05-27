package com.example.exercise2.service.model;

import java.util.Date;
import java.util.Objects;


public class Author extends BaseModel {

  private Long id;
  private String name;

  public Author() {
  }

  public Author(Long id, String name) {
    this.id = id;
    this.name = name;
  }

  public Author(Long id, String name, Date createdDate, Date lastModifiedDate) {
    super(createdDate, lastModifiedDate);
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Author author = (Author) o;
    return Objects.equals(id, author.id) && Objects.equals(name, author.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name);
  }
}
