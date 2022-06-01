package com.masters.masters.exercise.controller;

import com.masters.masters.exercise.exception.RecordNotFoundException;
import com.masters.masters.exercise.model.BookDtoRequest;
import com.masters.masters.exercise.model.BookDtoResponse;
import com.masters.masters.exercise.model.BookEntity;
import com.masters.masters.exercise.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/book")
public class BookController {

    @Autowired
    private BookService bookService;


    @GetMapping
    public ResponseEntity<Page<BookEntity>> getAllBooks(@RequestParam(name="title") String tile, Pageable pageRequest) throws RecordNotFoundException {
        Page<BookEntity> list = bookService.findBookByTitle(tile,pageRequest);
        return new ResponseEntity<Page<BookEntity>>(list, new HttpHeaders(), HttpStatus.OK);
    }


}
