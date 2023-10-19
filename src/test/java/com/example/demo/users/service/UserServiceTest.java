package com.example.demo.users.service;


import com.example.demo.users.model.Role;
import com.example.demo.users.model.User;
import com.example.demo.users.repository.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock private UserRepo userRepo;
    private UserService underTest;
    @Mock private UserDTOMapper userDTOMapper;

    @BeforeEach
    void setUp() {
        underTest = new UserService(userRepo, userDTOMapper);
    }

    @Test
    void canGetAllUsers() {
        //when
        underTest.getAllUsers();
        //then
        verify(userRepo).findAll();
    }

    @Test
    void canFindByUsername() {
        String username = "test";
        //when
        underTest.findByUsernameOrEmail(username);
        //then
        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(userRepo).findAllByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(stringArgumentCaptor.capture(), stringArgumentCaptor.capture());

        String result = stringArgumentCaptor.getValue();

        assertEquals(username,result);
    }

}