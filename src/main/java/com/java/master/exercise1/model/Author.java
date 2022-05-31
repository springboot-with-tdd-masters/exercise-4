package com.java.master.exercise1.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Author extends AuditModel {

	private static final long serialVersionUID = 5596881367802476483L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	Long id;

	String name;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "author")
	Set<Book> books;

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
}
