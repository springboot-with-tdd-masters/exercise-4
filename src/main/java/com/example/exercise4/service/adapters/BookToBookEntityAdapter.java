package com.example.exercise4.service.adapters;

import com.example.exercise4.repository.entity.BookEntity;
import com.example.exercise4.service.model.Book;

public interface BookToBookEntityAdapter {

  BookEntity convert(Book book);
}
