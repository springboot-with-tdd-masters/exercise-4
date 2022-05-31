package com.java.master.exercise1.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.java.master.exercise1.model.User;

@Service(value = "userService")
public interface UserService {
	
	public List<User> findAll();
	
	public void delete(long id);
	
	public User save(User user);
}
