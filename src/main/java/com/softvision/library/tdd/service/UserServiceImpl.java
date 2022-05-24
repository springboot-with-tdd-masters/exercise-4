package com.softvision.library.tdd.service;

import com.softvision.library.tdd.model.User;
import com.softvision.library.tdd.model.UserNotFoundException;
import com.softvision.library.tdd.repository.UserRepository;
import com.softvision.library.tdd.utils.UserDetailsAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
        return new UserDetailsAdapter(user);
    }

}
