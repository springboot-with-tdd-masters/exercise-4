package com.softvision.books.repositories.entities;

import javax.persistence.Entity;

@Entity
public class AuthorEntity extends AbstractEntity {

    private String name;

    public AuthorEntity() {}

    public AuthorEntity(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
