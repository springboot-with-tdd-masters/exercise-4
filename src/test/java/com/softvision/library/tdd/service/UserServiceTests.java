package com.softvision.library.tdd.service;

import com.softvision.library.tdd.model.UserNotFoundException;
import com.softvision.library.tdd.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import static java.util.Optional.*;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static com.softvision.library.tdd.LibraryMocks.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

    @Mock
    UserRepository mockUserRepository;

    @Mock
    PasswordEncoder mockPasswordEncoder;

    @InjectMocks
    UserService userService = new UserServiceImpl();

    @Test
    @DisplayName("Load User by Username - should return an implementation of UserDetails")
    void test_loadUserByUsername() {
        when(mockUserRepository.findByUsername(MOCK_USER1_USERNAME)).thenReturn(of(getMockUser1()));
        UserDetails actualUserDetails = userService.loadUserByUsername(MOCK_USER1_USERNAME);

        assertEquals(MOCK_USER1_USERNAME, actualUserDetails.getUsername());
        assertThat(actualUserDetails.getAuthorities())
                .extracting(GrantedAuthority::getAuthority)
                .containsExactly(MOCK_USER1_ROLE);
    }

    @Test
    @DisplayName("Load User by Username - should throw a UserNotFoundException when username is not found")
    void test_loadUserByUsername_userNotFound() {
        when(mockUserRepository.findByUsername(MOCK_USER1_USERNAME)).thenReturn(empty());
        assertThrows(UserNotFoundException.class, () -> userService.loadUserByUsername(MOCK_USER1_USERNAME));
    }
}
