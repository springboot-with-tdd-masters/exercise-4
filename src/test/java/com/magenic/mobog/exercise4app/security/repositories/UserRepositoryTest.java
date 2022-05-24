package com.magenic.mobog.exercise4app.security.repositories;

import com.magenic.mobog.exercise4app.security.entities.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository repository;

    @AfterEach
    void teardown(){
        this.entityManager.clear();
        this.entityManager.flush();
    }

    @Test
    @DisplayName("Should be able to save entity")
    void shouldSaveEntity(){
        User user = new User();
        user.setUserName("jdoe");
        user.setPassword("jdoemapagmahal");
        User saved = repository.save(user);

        User actual = entityManager.find(User.class, saved.getUserId());

        assertNotNull(actual);
        assertEquals(user.getUserName(), actual.getUserName());
        assertEquals(user.getPassword(), actual.getPassword());
    }
    @Test
    @DisplayName("Should be able to retrieve entity by username")
    void shouldFindEntityByUserName(){
        User user = new User();
        user.setUserName("jdoe");
        user.setPassword("jdoemapagmahal");
        entityManager.persist(user);

        Optional<User> actual = repository.findByUserName("jdoe");

        assertNotNull(actual);
        User actualGet = actual.get();
        assertEquals(user.getUserName(), actualGet.getUserName());
        assertEquals(user.getPassword(), actualGet.getPassword());
    }
}
