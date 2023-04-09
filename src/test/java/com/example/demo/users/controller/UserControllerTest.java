package com.example.demo.users.controller;

import com.example.demo.users.repository.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.assertj.core.api.Assertions.*;

import java.io.File;
import java.nio.file.Files;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {


    @Autowired
    private UserRepo userRepo;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    @Test
    @WithMockUser(authorities ={"ADMIN"})
    @SqlGroup({
            @Sql(value = "classpath:empty/reset.sql", executionPhase = BEFORE_TEST_METHOD),
            @Sql(value = "classpath:init/user/user-data.sql", executionPhase = BEFORE_TEST_METHOD)
    })
    void canGetAllUsers() throws Exception {
        this.mockMvc.perform(get("/users"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(5)))
                .andExpect(jsonPath("$.[0].id").value(1))
                .andExpect(jsonPath("$.[1].id").value(2))
                .andExpect(jsonPath("$.[2].id").value(3))
                .andExpect(jsonPath("$.[3].id").value(4))
                .andExpect(jsonPath("$.[4].id").value(5));
    }


    @WithMockUser(authorities ={"USER"})
    @SqlGroup({
            @Sql(value = "classpath:empty/reset.sql", executionPhase = BEFORE_TEST_METHOD),
            @Sql(value = "classpath:init/user/user-data.sql", executionPhase = BEFORE_TEST_METHOD)
    })
    @Test
    void canGetUserById() throws Exception {
        this.mockMvc.perform(get("/users/find/RogerClara"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$").isMap())
                .andExpect(jsonPath("$.username").value("RogerClara"));
    }

    @WithMockUser(authorities ={"USER"})
    @SqlGroup({
            @Sql(value = "classpath:empty/reset.sql", executionPhase = BEFORE_TEST_METHOD),
            @Sql(value = "classpath:init/user/user-data.sql", executionPhase = BEFORE_TEST_METHOD)
    })
    @Test
    void canUpdateUser() throws Exception {
        //given
        final File jsonFile = new ClassPathResource("init/user/user-edit.json").getFile();
        final String userToUpdate = Files.readString(jsonFile.toPath());
        //when
        this.mockMvc.perform(put("/users/update")
                .contentType(APPLICATION_JSON)
                .content(userToUpdate))
                .andDo(print())
                .andExpect(jsonPath("$").isMap())
                .andExpect(jsonPath("$.username").value("RogerUpdated"));
        //then
        assertThat(userRepo.findAll()).hasSize(5);

    }

    @WithMockUser(authorities ={"ADMIN"})
    @SqlGroup({
            @Sql(value = "classpath:empty/reset.sql", executionPhase = BEFORE_TEST_METHOD),
            @Sql(value = "classpath:init/user/user-data.sql", executionPhase = BEFORE_TEST_METHOD)
    })
    @Test
    void canSearchUser() throws Exception {
        this.mockMvc.perform(get("/users/search?query=alicia"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.[0].username").value("RogerClara"));
    }

    @WithMockUser(authorities ={"ADMIN"})
    @SqlGroup({
            @Sql(value = "classpath:empty/reset.sql", executionPhase = BEFORE_TEST_METHOD),
            @Sql(value = "classpath:init/user/user-data.sql", executionPhase = BEFORE_TEST_METHOD)
    })
    @Test
    void canDeleteUser() throws Exception {
        //given
        int id =1;
        //when
        this.mockMvc.perform(delete("/users/delete/"+id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("User with ID: "+id+" was deleted"));
        //then
        assertThat(userRepo.findAll()).hasSize(4);
    }
}