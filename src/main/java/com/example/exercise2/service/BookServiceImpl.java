package com.example.exercise2.service;

import com.example.exercise2.exception.BookNotFoundException;
import com.example.exercise2.repository.BookRepository;
import com.example.exercise2.repository.entity.BookEntity;
import com.example.exercise2.service.adapters.BookEntityToBookAdapter;
import com.example.exercise2.service.adapters.BookToBookEntityAdapter;
import com.example.exercise2.service.model.Book;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

@Service
public class BookServiceImpl implements BookService {

  @Autowired
  private BookRepository bookRepository;

  @Autowired
  private BookToBookEntityAdapter bookToBookEntityAdapter;

  @Autowired
  private BookEntityToBookAdapter bookEntityToBookAdapter;


  @Override
  public Book save(Book book) {
    BookEntity bookToSave = bookToBookEntityAdapter.convert(book);
    BookEntity savedBookEntity = bookRepository.save(bookToSave);
    return bookEntityToBookAdapter.convert(savedBookEntity);
  }

  @Override
  public List<Book> findAll() {
    return bookEntityToBookAdapter.convert(bookRepository.findAll());
  }

  @Override
  public Page<Book> findAll(int page, int size, String[] sort) {
    Pageable pagingSort = PageRequest.of(page, size, Sort.by(getSortOrder(sort)));
    Page<BookEntity> bookEntityPage = bookRepository.findAll(pagingSort);
    return bookEntityToBookAdapter.convert(bookEntityPage);
  }

  @Override
  public Page<Book> findBooksByTitleContaining(String title, int page, int size, String[] sort) {
    Pageable pagingSort = PageRequest.of(page, size, Sort.by(getSortOrder(sort)));
    Page<BookEntity> bookEntityPage = bookRepository.findByTitleContaining(title,pagingSort);
    return bookEntityToBookAdapter.convert(bookEntityPage);
  }


  @Override
  public Book findById(long bookId) {
    Optional<BookEntity> bookEntity = bookRepository.findById(bookId);
    if (bookEntity.isPresent()) {
      return bookEntityToBookAdapter.convert(bookEntity.get());
    } else {
      throw new BookNotFoundException();
    }
  }

  private List<Order> getSortOrder(String[] sort) {
    List<Order> orders = new ArrayList<Order>();
    if (sort[0].contains(",")) {
      for (String sortOrder : sort) {
        String[] _sort = sortOrder.split(",");
        orders.add(new Order(getSortDirection(_sort[1]), _sort[0]));
      }
    } else {
      orders.add(new Order(getSortDirection(sort[1]), sort[0]));
    }
    return orders;
  }

  private Sort.Direction getSortDirection(String direction) {
    if (direction.equals("asc")) {
      return Sort.Direction.ASC;
    } else if (direction.equals("desc")) {
      return Sort.Direction.DESC;
    }
    return Sort.Direction.ASC;
  }
}
