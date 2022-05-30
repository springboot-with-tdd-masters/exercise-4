package com.example.exercise2.service.model;

import java.util.Date;

public abstract class BaseModel {

  private Date createdDate;
  private Date lastModifiedDate;

  public BaseModel() {
  }

  public BaseModel(Date createdDate, Date lastModifiedDate) {
    this.createdDate = createdDate;
    this.lastModifiedDate = lastModifiedDate;
  }

  public Date getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(Date createdDate) {
    this.createdDate = createdDate;
  }

  public Date getLastModifiedDate() {
    return lastModifiedDate;
  }

  public void setLastModifiedDate(Date lastModifiedDate) {
    this.lastModifiedDate = lastModifiedDate;
  }
}

