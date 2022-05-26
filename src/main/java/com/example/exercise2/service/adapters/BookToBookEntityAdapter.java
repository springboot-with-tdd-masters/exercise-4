package com.example.exercise2.service.adapters;

import com.example.exercise2.repository.entity.BookEntity;
import com.example.exercise2.service.model.Book;

public interface BookToBookEntityAdapter {

  BookEntity convert(Book book);
}
