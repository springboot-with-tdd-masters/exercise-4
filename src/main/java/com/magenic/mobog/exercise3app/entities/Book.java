package com.magenic.mobog.exercise3app.entities;

import com.magenic.mobog.exercise3app.entities.audit.BaseEntity;

import javax.persistence.*;

@Entity
public class Book extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column
	private String title;
	@ManyToOne
	private Author author;

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

	public void setAuthor(Author author) {
		this.author = author;
	}

	public Author getAuthor() {
		return author;
	}
}
