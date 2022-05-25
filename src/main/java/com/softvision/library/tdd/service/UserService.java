package com.softvision.library.tdd.service;
import com.softvision.library.tdd.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    User createUser(User user);
}
