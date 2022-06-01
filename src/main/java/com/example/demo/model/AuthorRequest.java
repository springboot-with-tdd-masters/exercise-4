package com.example.demo.model;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class AuthorRequest {

    @NotEmpty
    private List<String> books;

    @NotEmpty
    private String name;
}
