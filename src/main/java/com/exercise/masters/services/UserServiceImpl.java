/**
 * 
 */
package com.exercise.masters.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.exercise.masters.exceptions.AccountExistException;
import com.exercise.masters.model.UserEntity;
import com.exercise.masters.repository.UserRepository;

/**
 * @author michaeldelacruz
 *
 */

@Service
public class UserServiceImpl implements UserService {


	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder bcryptEncoder;

	@Override
	public UserEntity register(UserEntity userEntity) throws AccountExistException {
		Optional<UserEntity> user = userRepository.findByUsername(userEntity.getUsername());
		
		if(user.isPresent()) {
			throw new AccountExistException(userEntity.getUsername() + " is already exist.");
		}
		
		UserEntity entity = new UserEntity();
		entity.setUsername(userEntity.getUsername());
		entity.setPassword(bcryptEncoder.encode(userEntity.getPassword()));
		
		return userRepository.save(entity);
	}
	
	
	
}
