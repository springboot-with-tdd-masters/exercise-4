package com.example.exercisefour.service;

import com.example.exercisefour.model.Users;

public interface UserService {
	
	Users saveUser(Users user);
	void deleteUser(long id);
}
