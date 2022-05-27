package com.magenic.mobog.exercise4app.security.services;

import com.magenic.mobog.exercise4app.exceptions.InternalServerException;
import com.magenic.mobog.exercise4app.exceptions.UserExistsException;
import com.magenic.mobog.exercise4app.security.entities.User;
import com.magenic.mobog.exercise4app.security.repositories.UserRepository;
import com.magenic.mobog.exercise4app.security.requests.RegisterUserReqDto;
import com.magenic.mobog.exercise4app.security.responses.RegisterUserResDto;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MyUserDetailsServiceTest {

    private MyUserDetailsService service;

    @Mock
    private PasswordEncoder mockEncoder;
    @Mock
    private UserRepository repository;

    @BeforeEach
    void setup() {
        service = new MyUserDetailsService(repository, mockEncoder);
    }

    @Test
    @DisplayName("should load by userName successfully")
    void shouldLoadByUserNameSuccessfully() {
        User stubSaved = new User();
        stubSaved.setUserId("1235467");
        stubSaved.setPassword("jdoegenerated");
        stubSaved.setUserName("jdoe");
        when(repository.findByUserName("jdoe")).thenReturn(Optional.of(stubSaved));
        UserDetails actual = service.loadUserByUsername("jdoe");
        assertNotNull(actual);
        assertEquals("jdoe", actual.getUsername());
        assertEquals("jdoegenerated", actual.getPassword());
    }

    @Test
    @DisplayName("should throw user not found exception")
    void shouldThrowUserNotFoundExceptionWhenNotFound() {
        when(repository.findByUserName("jdoe")).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> service.loadUserByUsername("jdoe"));
    }

    @Test
    @DisplayName("should register user successfully")
    void shouldSaveUserSuccessfully() {
        when(repository.findByUserName("jdoe")).thenReturn(Optional.empty());
        when(mockEncoder.encode("jdoemapagmahal")).thenReturn("1234654787798797");
        User stubSaved = new User();
        stubSaved.setUserId("jdoegenerated");
        stubSaved.setUserName("jdoe");
        when(repository.save(any(User.class))).thenReturn(stubSaved);
        // when
        RegisterUserReqDto req = new RegisterUserReqDto();
        req.setUsername("jdoe");
        req.setPassword("jdoemapagmahal");
        RegisterUserResDto actual = service.registerUser(req);
        // then
        assertNotNull(actual);
        assertEquals("jdoe", actual.getUsername());
        assertEquals("jdoegenerated", actual.getUserId());


    }

    @Test
    @DisplayName("should throw exception for existing user")
    void shouldThrowExceptionForExistingUserName() {
        // given
        User stubSaved = new User();
        stubSaved.setUserId("1235467");
        stubSaved.setPassword("jdoegenerated");
        stubSaved.setUserName("jdoe");
        when(repository.findByUserName("jdoe")).thenReturn(Optional.of(stubSaved));
        // when
        RegisterUserReqDto req = new RegisterUserReqDto();
        req.setUsername("jdoe");
        req.setPassword("jdoemapagmahal");
        // then
        assertThrows(UserExistsException.class, () -> service.registerUser(req));

    }

    @Test
    @DisplayName("should throw exception for unable to saved user")
    void shouldThrowExceptionForUnableToSaveUser() {
        // given
        when(repository.findByUserName("jdoe")).thenReturn(Optional.empty());
        when(mockEncoder.encode(anyString())).thenThrow(new NullPointerException());
        // when
        RegisterUserReqDto req = new RegisterUserReqDto();
        req.setUsername("jdoe");
        req.setPassword("jdoemapagmahal");
        // then
        assertThrows(InternalServerException.class, () -> service.registerUser(req));

    }

}
