package com.magenic.mobog.exercise3app.adapters;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.magenic.mobog.exercise3app.responses.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.magenic.mobog.exercise3app.entities.Book;
import com.magenic.mobog.exercise3app.exceptions.InvalidRequestException;
import com.magenic.mobog.exercise3app.requests.AddBookRequest;
import com.magenic.mobog.exercise3app.responses.BookResponse;

@Service
public class BookAdapter {

	public BookResponse mapBookToResponse(Book book) {
		return BookResponse
				.builder()
				.id(book.getId())
				.authorId(book.getAuthor().getId())
				.title(book.getTitle())
				.createdAt(book.getCreatedDate().toString())
				.updatedAt(book.getLastModifiedDate().toString())
				.build();
	}

	public Book mapRequestToEntity(AddBookRequest book) {
		if (Optional.ofNullable(book.getTitle()).isPresent()) {
			Book entity = new Book();
			entity.setTitle(book.getTitle());

			return entity;
		}
		throw new InvalidRequestException();
	}
	public PageResponse<BookResponse> mapPageToPageResponse(Page<Book> page){
		List<BookResponse> bookResponseList = page.get().map(this::mapBookToResponse).collect(Collectors.toList());
		Pageable pageReq = page.getPageable();
		return PageResponse.<BookResponse>builder()
				.size(page.getSize())
				.page(pageReq.getPageNumber())
				.size(pageReq.getPageSize())
				.content(bookResponseList)
				.totalPage(page.getTotalPages())
				.totalElement(page.getTotalElements())
				.build();

	}

}
