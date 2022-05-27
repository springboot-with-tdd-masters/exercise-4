/**
 * 
 */
package com.exercise.masters.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.exercise.masters.model.UserEntity;

/**
 * @author michaeldelacruz
 *
 */

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long>{

	public Optional<UserEntity> findByUsername(String username);
	
}
