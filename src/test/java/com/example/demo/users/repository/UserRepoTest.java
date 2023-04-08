package com.example.demo.users.repository;

import com.example.demo.users.model.Role;
import com.example.demo.users.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepoTest {

    @Autowired
    private UserRepo underTest;

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @BeforeEach
    void setUp() {

    }

    @Test
    void existsUserByUsername() {
        String username = "testUser";
        User user = new User(null,"test","testing","test@test.com",username,"teeest", Role.USER,"url","18","0655678230","Morocco");
        underTest.save(user);

        //when
        boolean result = underTest.existsUserByUsername(username);

        //then
        assertTrue(result);
    }

    @Test
    void UserDoesNotExistByUsername() {
        String username = "testUser";
//        User user = new User(null,"test","testing","test@test.com",username,"teeest", Role.USER,"url","18","0655678230","Morocco");
//        underTest.save(user);

        //when
        boolean result = underTest.existsUserByUsername(username);

        //then
        assertFalse(result);
    }

    @Test
    void findUserByUsername() {
        String username = "testUser";
        User user = new User(null,"test","testing","test@test.com",username,"teeest", Role.USER,"url","18","0655678230","Morocco");
        underTest.save(user);

        //when
        User result = underTest.findByUsername(username).orElseThrow();

        //then
        assertEquals(user,result);
    }

    @Test
    void findAllByUsernameContainingIgnoreCase() {
        String username = "testUser";
        User user = new User(null,"test","testing","test@test.com",username,"teeest", Role.USER,"url","18","0655678230","Morocco");
        underTest.save(user);
        List<User> users = new ArrayList<User>();
        users.add(user);

        //when
        Optional<List<User>> result = underTest.findAllByUsernameContainingIgnoreCase("User");

        //then
        assertEquals(Optional.of(users),result);
    }
}