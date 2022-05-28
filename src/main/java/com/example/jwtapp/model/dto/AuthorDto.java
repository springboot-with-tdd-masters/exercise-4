package com.example.jwtapp.model.dto;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.example.jwtapp.model.Author;


public class AuthorDto {
	private Date createdDate;
	private Date updatedDate;
	private Long id;
	private String name;
	
	public AuthorDto() {};
	
	public AuthorDto(Author author) {
		id = author.getId();
		name = author.getName();
		createdDate = author.getCreatedDate();
		updatedDate = author.getCreatedDate();
	};
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	
	public static AuthorDto convertToDto(Author author) {
		return new AuthorDto(author);
	}
}
