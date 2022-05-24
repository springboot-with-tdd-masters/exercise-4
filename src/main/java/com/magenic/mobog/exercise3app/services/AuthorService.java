package com.magenic.mobog.exercise3app.services;

import com.magenic.mobog.exercise3app.adapters.AuthorAdapter;
import com.magenic.mobog.exercise3app.adapters.BookAdapter;
import com.magenic.mobog.exercise3app.entities.Author;
import com.magenic.mobog.exercise3app.entities.Book;
import com.magenic.mobog.exercise3app.exceptions.EntityNotFoundException;
import com.magenic.mobog.exercise3app.exceptions.InvalidRequestException;
import com.magenic.mobog.exercise3app.repositories.AuthorRepository;
import com.magenic.mobog.exercise3app.repositories.BookRepository;
import com.magenic.mobog.exercise3app.requests.AddBookRequest;
import com.magenic.mobog.exercise3app.requests.AddAuthorRequest;
import com.magenic.mobog.exercise3app.responses.AuthorResponse;
import com.magenic.mobog.exercise3app.responses.BookResponse;
import com.magenic.mobog.exercise3app.responses.PageResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthorService {
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final BookAdapter bookAdapter;
    private final AuthorAdapter authorAdapter;

    public AuthorService(AuthorRepository authorRepository,
                         BookRepository bookRepository,
                         AuthorAdapter authorAdapter,
                         BookAdapter bookAdapter) {
        this.authorAdapter = authorAdapter;
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
        this.bookAdapter = bookAdapter;
    }

    public AuthorResponse createAuthor(AddAuthorRequest request) {
        return Optional.ofNullable(request)
                .map(authorAdapter::mapRequestToAuthorEntity)
                .map(authorRepository::save)
                .map(authorAdapter::mapAuthorToResponse)
                .orElseThrow(InvalidRequestException::new);
    }

    public BookResponse addBookToAuthor(Long authorId, AddBookRequest addBookRequest) {
        Optional<Author> foundAuthor = authorRepository.findById(authorId);
        if (foundAuthor.isPresent()) {
            try {
                Book newBook = this.bookAdapter.mapRequestToEntity(addBookRequest);
                newBook.setAuthor(foundAuthor.get());
                Book saved = bookRepository.save(newBook);
                return bookAdapter.mapBookToResponse(saved);
            } catch (Exception e) { // catch all
                throw new InvalidRequestException();
            }
        }
        throw new EntityNotFoundException();
    }

    public PageResponse<AuthorResponse> findAuthorsWithPage(Pageable page) {
        return Optional.ofNullable(this.authorRepository.findAll(page))
                .map(authorAdapter::mapPageToPageResponse)
                .orElseThrow(InvalidRequestException::new);
    }

    public PageResponse<BookResponse> findBookByAuthorWithPage(Long authorId, Pageable page) {
        return Optional.ofNullable(this.bookRepository.findByAuthorId(authorId, page))
                .map(bookAdapter::mapPageToPageResponse)
                .orElse(PageResponse.<BookResponse>builder().build());
    }

    public PageResponse<BookResponse> findBooksContainingTitle(String partialTitle, Pageable page) {
        return Optional.ofNullable(this.bookRepository.findByTitleContaining(partialTitle, page))
                .map(bookAdapter::mapPageToPageResponse)
                .orElse(PageResponse.<BookResponse>builder().build());
    }

    public BookResponse getBookMadeByAuthor(Long id, Long authorId) {

        return Optional.ofNullable(this.bookRepository.findByIdAndAuthorId(id, authorId))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(bookAdapter::mapBookToResponse)
                .orElseThrow(EntityNotFoundException::new);
    }

    public AuthorResponse getAuthorById(Long id) {
        return Optional.ofNullable(this.authorRepository.findById(id))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(authorAdapter::mapAuthorToResponse)
                .orElseThrow(EntityNotFoundException::new);
    }
}
