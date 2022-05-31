package com.example.exercisefour.response;

import com.example.exercisefour.exception.LibraryAppException;
import com.example.exercisefour.exception.LibraryAppExceptionCode;
import com.example.exercisefour.model.Book;

public class BookResponse {
	
	private String createdAt;
	private String updatedAt;
	private Long id;
	private String title;
	private String description;
	private Long authorId;
	public String getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
	public String getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Long getAuthorId() {
		return authorId;
	}
	public void setAuthorId(Long authorId) {
		this.authorId = authorId;
	}
	
	public static BookResponse convertToBookResponse(Book book) {
		try {
			BookResponse response = new BookResponse();
			response.setCreatedAt(book.getCreatedDate().toString());
			response.setUpdatedAt(book.getLastModifiedDate().toString());
			response.setId(book.getId());
			response.setTitle(book.getTitle());
			response.setDescription(book.getDescription());
			response.setAuthorId(book.getAuthor().getId());
			
			return response;

		} catch (Exception e) {
			throw new LibraryAppException(LibraryAppExceptionCode.UNABLE_TO_MAP_EXCEPTION);
		}
			}
}
