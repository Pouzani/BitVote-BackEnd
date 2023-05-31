package com.example.demo.auth.service;

import com.example.demo.auth.model.AuthenticationRequest;
import com.example.demo.auth.model.AuthenticationResponse;
import com.example.demo.config.JwtService;
import com.example.demo.users.model.Role;
import com.example.demo.users.model.User;
import com.example.demo.users.repository.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.assertj.core.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private UserRepo userRepo;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;
    private AuthenticationService underTest;

    @BeforeEach
    void setUp() {
        this.underTest = new AuthenticationService(authenticationManager,passwordEncoder,userRepo,jwtService);
    }

    @Test
    void register() {
        //given
        String username = "testUser";
        User user = new User(null,"test","testing","test@test.com",username,passwordEncoder.encode("teeest"), Role.USER,"url",18,"0655678230","Morocco");
        //when
        underTest.register(user);
        //then
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        ArgumentCaptor<User> userArgumentCaptor2 = ArgumentCaptor.forClass(User.class);
        verify(userRepo).save(userArgumentCaptor.capture());
        verify(jwtService).generateToken(userArgumentCaptor2.capture());

        User result = userArgumentCaptor.getValue();
        User result2 = userArgumentCaptor2.getValue();
        assertEquals(user,result);
        assertEquals(user,result2);

    }

    @Test
    void authenticate() {
        //given
        AuthenticationRequest request = new AuthenticationRequest("test","tessst");
        given(userRepo.existsUserByUsername(request.getUsername())).willReturn(true);
        //when
        underTest.authenticate(request);
        //then
        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<UsernamePasswordAuthenticationToken> argumentCaptor = ArgumentCaptor.forClass(UsernamePasswordAuthenticationToken.class);
        verify(userRepo).findByUsername(stringArgumentCaptor.capture());
        verify(authenticationManager).authenticate(argumentCaptor.capture());

        String username = stringArgumentCaptor.getValue();
        UsernamePasswordAuthenticationToken result = argumentCaptor.getValue();
        assertEquals(request.getUsername(),username);
        assertEquals(new UsernamePasswordAuthenticationToken(request.getUsername(),request.getPassword()),result);

    }
}