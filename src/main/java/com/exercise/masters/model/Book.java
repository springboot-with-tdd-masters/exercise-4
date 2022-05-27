/**
 * 
 */
package com.exercise.masters.model;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

/**
 * @author michaeldelacruz
 *
 */

@Entity
@EntityListeners(AuditingEntityListener.class)
public class Book extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public Book() {
		
	}

	/**
	 * @param id
	 * @param name
	 * @param authorDto
	 */
	public Book(Long id, String name, Author author) {
		super();
		this.id = id;
		this.name = name;
		this.author = author;
	}

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String name;

	@ManyToOne
	@JsonProperty("authorId")
    @JsonIdentityReference(alwaysAsId = true)
    @JoinColumn(name="authorId", nullable=false)
	@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
	private Author author;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the author
	 */
	public Author getAuthor() {
		return author;
	}

	/**
	 * @param author the author to set
	 */
	public void setAuthor(Author author) {
		this.author = author;
	}
	
}
