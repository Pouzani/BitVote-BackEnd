package com.example.demo.auth.controller;

import com.example.demo.users.model.Role;
import com.example.demo.users.model.User;
import com.example.demo.users.repository.UserRepo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthenticationControllerTest {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    @AfterEach
    void tearDown() {
        userRepo.deleteAll();
    }

    @Test
    void register() throws Exception {
        //given
        final File jsonFile = new ClassPathResource("init/auth/register.json").getFile();
        final String authRequest = Files.readString(jsonFile.toPath());
        //when
        this.mockMvc.perform(post("/auth/register")
                .contentType(APPLICATION_JSON)
                .content(authRequest))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").isMap())
                .andExpect(jsonPath("$.username").exists())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.role").exists());

        assertThat(userRepo.findAll()).hasSize(1);

    }

    @Test
    void login() throws Exception {
        //given
        User user = new User(null,null,null,"test.test","test",passwordEncoder.encode("testpass"), Role.USER,null,null,null,null);
        userRepo.save(user);
        final File jsonFile = new ClassPathResource("init/auth/authenticate.json").getFile();
        final String authRequest = Files.readString(jsonFile.toPath());
        //when
        this.mockMvc.perform(post("/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(authRequest))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isMap())
                .andExpect(jsonPath("$.username").exists())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.role").exists());

    }
}