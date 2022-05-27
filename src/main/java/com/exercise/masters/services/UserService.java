/**
 * 
 */
package com.exercise.masters.services;

import com.exercise.masters.exceptions.AccountExistException;
import com.exercise.masters.model.UserEntity;

/**
 * @author michaeldelacruz
 *
 */
public interface UserService {
	
	public UserEntity register(UserEntity userEntity) throws AccountExistException;
	
}
