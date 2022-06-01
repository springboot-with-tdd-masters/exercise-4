package com.example.demo.model;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class BookRequest {

    @NotEmpty
    private String title;

    @NotEmpty
    private String author;
}
