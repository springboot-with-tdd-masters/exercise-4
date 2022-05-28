package com.example.jwtapp.model.dto;

import java.util.Date;

import com.example.jwtapp.model.Book;

public class BookDto {

	private Date createdDate;
	private Date updatedDate;
	private Long id;
	private String title;
	private String description;
	private Long authorId;
	
	public BookDto() {}
	
	public BookDto(Book book) {
		createdDate = book.getCreatedDate();
		updatedDate = book.getUpdatedDate();
		id = book.getId();
		title = book.getTitle();
		description = book.getDescription();
		authorId = book.getAuthor().getId();
	}
	
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public Date getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
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
	
	public static BookDto convertToDto(Book book) {
		return new BookDto(book);
	}
}
