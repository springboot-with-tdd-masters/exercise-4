package com.softvision.library.tdd.repository;

import com.softvision.library.tdd.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static com.softvision.library.tdd.LibraryMocks.*;
import static org.assertj.core.api.Assertions.*;

@DataJpaTest
public class UserRepositoryTests {

    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("should get the exact saved user given its username")
    void test_findByUsername() {
        User expected = userRepository.save(getMockUser1());

        assertThat(userRepository.findByUsername(MOCK_USER1_USERNAME))
                .map(User::getId)
                .get().isEqualTo(expected.getId());
    }
}
