package com.java.master.exercise1.service.impl;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.java.master.exercise1.model.User;
import com.java.master.exercise1.repository.UserRepository;
import com.java.master.exercise1.service.UserService;

@Service(value = "userService")
public class UserServiceImpl implements UserDetailsService, UserService {

	@Autowired
	UserRepository userRepository;
	
	@Override
	public List<User> findAll() {
		return userRepository.findAll();
	}

	@Override
	public void delete(long id) {
		userRepository.deleteById(id);
	}

	@Override
	public User save(User user) {
		return userRepository.save(user);
	}

	@Override
	public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(userId);
		if(user == null)
			throw new UsernameNotFoundException("Invalid username or password");
		
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), getAuthority());
	}

	private List<SimpleGrantedAuthority> getAuthority() {
		return Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"));
	}
}
