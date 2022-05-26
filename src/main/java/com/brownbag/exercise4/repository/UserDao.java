package com.brownbag.exercise4.repository;

import com.brownbag.exercise4.entity.DaoUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends CrudRepository<DaoUser, Integer> {

    DaoUser findByUsername(String username);
}
