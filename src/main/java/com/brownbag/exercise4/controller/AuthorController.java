package com.brownbag.exercise4.controller;


import com.brownbag.exercise4.entity.Author;
import com.brownbag.exercise4.errorhandler.AuthorNotFoundException;
import com.brownbag.exercise4.helpers.SortHelpers;
import com.brownbag.exercise4.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/authors")
public class AuthorController {

    @Autowired
    private AuthorService authorService;

    @GetMapping("/{id}")
    public ResponseEntity<Author> findById(@PathVariable Integer id) throws AuthorNotFoundException {

        return new ResponseEntity<Author>(authorService.findById(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<Author>> find(@RequestParam(defaultValue = "0") Integer page,
                                             @RequestParam(defaultValue = "10") Integer size,
                                             @RequestParam(defaultValue = "name") String sortBy,
                                             @RequestParam(defaultValue = "true") Boolean isAsc)
            throws AuthorNotFoundException {

        return new ResponseEntity<Page<Author>>(authorService.find(page, size,
                SortHelpers.getSort(sortBy, isAsc)), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Integer id) throws AuthorNotFoundException {
        return new ResponseEntity<>(authorService.delete(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity save(@RequestBody Author author) {
        return new ResponseEntity<>(authorService.save(author), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    private ResponseEntity<Author> put(@PathVariable Integer id, @RequestBody Author author) throws
            AuthorNotFoundException{
        return new ResponseEntity<>(authorService.update(id, author), HttpStatus.OK);
    }
}
