package com.magenic.mobog.exercise4app.security.services;

import com.magenic.mobog.exercise4app.exceptions.InternalServerException;
import com.magenic.mobog.exercise4app.exceptions.UserExistsException;
import com.magenic.mobog.exercise4app.security.entities.User;
import com.magenic.mobog.exercise4app.security.entities.UserWrapper;
import com.magenic.mobog.exercise4app.security.repositories.UserRepository;
import com.magenic.mobog.exercise4app.security.requests.RegisterUserReqDto;
import com.magenic.mobog.exercise4app.security.responses.RegisterUserResDto;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserDetailsService implements UserDetailsService {

    private UserRepository repository;
    private PasswordEncoder bcryptEncoder;

    public MyUserDetailsService(UserRepository repository, PasswordEncoder bcryptEncoder){
        this.repository = repository;
        this.bcryptEncoder = bcryptEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> found = this.repository.findByUserName(username);
        if(found.isPresent()){
            User get = found.get();
            return UserWrapper.builder()
                    .userId(get.getUserId())
                    .userName(get.getUserName())
                    .password(get.getPassword())
                    .build();

        }
        throw new UsernameNotFoundException("Not found");
    }
    public RegisterUserResDto registerUser(RegisterUserReqDto userDto) {
        if(this.repository.findByUserName(userDto.getUsername()).isPresent()){
            throw new UserExistsException();
        }
        try {
            User newUser = new User();
            newUser.setUserName(userDto.getUsername());
            String password = bcryptEncoder.encode(userDto.getPassword());
            newUser.setPassword(password);

            User saved = this.repository.save(newUser);
            if(Optional.ofNullable(saved).isPresent()){
                return RegisterUserResDto.builder().username(saved.getUserName()).userId(saved.getUserId()).build();
            } else {
                throw new InternalServerException();
            }
        } catch (Exception e) {
            // TODO
            throw new InternalServerException();
        }

    }
}
