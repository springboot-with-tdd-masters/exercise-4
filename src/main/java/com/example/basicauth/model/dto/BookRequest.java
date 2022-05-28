package com.example.basicauth.model.dto;

import java.util.Objects;

public class BookRequest {
	
	private String title;
	private String description;
	
	public BookRequest() {}
	
	public BookRequest(String title, String description) {
		super();
		this.title = title;
		this.description = description;
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

	@Override
	public int hashCode() {
		return Objects.hash(description, title);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BookRequest other = (BookRequest) obj;
		return Objects.equals(description, other.description) && Objects.equals(title, other.title);
	}
	
}
