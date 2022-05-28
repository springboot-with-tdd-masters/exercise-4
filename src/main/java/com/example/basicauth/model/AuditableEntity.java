package com.example.basicauth.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public abstract class AuditableEntity implements Serializable{
	
	private static final long serialVersionUID = 5462223600L;
	
	@CreatedDate
	@Column(name = "created_date",  nullable = false, updatable = false)
	private Date createdDate;
	
	@LastModifiedDate
	@Column(name = "updated_date")
	private Date updatedDate;

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updateDate) {
		this.updatedDate = updateDate;
	}

	@Override
	public int hashCode() {
		return Objects.hash(createdDate, updatedDate);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AuditableEntity other = (AuditableEntity) obj;
		return Objects.equals(createdDate, other.createdDate) && Objects.equals(updatedDate, other.updatedDate);
	}
}
