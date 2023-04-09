package com.example.demo.forum.controller;

import com.example.demo.forum.repository.ForumRepo;
import org.junit.jupiter.api.BeforeEach;
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

import java.io.File;
import java.nio.file.Files;

import static org.hamcrest.Matchers.aMapWithSize;
import static org.hamcrest.Matchers.hasSize;
import static org.assertj.core.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class ForumControllerTest {

    @Autowired
    private ForumRepo forumRepo;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    @Test
    @SqlGroup({
            @Sql(value = "classpath:empty/reset.sql", executionPhase = BEFORE_TEST_METHOD),
            @Sql(value = "classpath:init/forum/forum-data.sql", executionPhase = BEFORE_TEST_METHOD)
    })
    void canGetAllForums() throws Exception {
        this.mockMvc.perform(get("/forum"))
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

    @Test
    @SqlGroup({
            @Sql(value = "classpath:empty/reset.sql", executionPhase = BEFORE_TEST_METHOD),
            @Sql(value = "classpath:init/forum/forum-data.sql", executionPhase = BEFORE_TEST_METHOD)
    })
    void canSearchForums() throws Exception {
        this.mockMvc.perform(get("/forum/search?query=alicia"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$.[0].id").value(1));
    }


    @Test
    @WithMockUser(authorities = {"USER"})
    @SqlGroup({
            @Sql(value = "classpath:empty/reset.sql", executionPhase = BEFORE_TEST_METHOD)
    })
    void canAddForum() throws Exception {
        //given
        final File jsonFile = new ClassPathResource("init/forum/forum.json").getFile();
        final String forumToCreate = Files.readString(jsonFile.toPath());
        //when
        this.mockMvc.perform(post("/forum/add")
                        .contentType(APPLICATION_JSON)
                        .content(forumToCreate))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isMap())
                .andExpect(jsonPath("$", aMapWithSize(4)));
        //then
        assertThat(forumRepo.findAll()).hasSize(1);
    }

    @Test
    @WithMockUser(authorities = {"USER"})
    @SqlGroup({
            @Sql(value = "classpath:empty/reset.sql", executionPhase = BEFORE_TEST_METHOD),
            @Sql(value = "classpath:init/forum/forum-data.sql", executionPhase = BEFORE_TEST_METHOD)
    })
    void canUpdateForum() throws Exception {
        //given
        final File jsonFile = new ClassPathResource("init/forum/forum-edit.json").getFile();
        final String forumToUpdate = Files.readString(jsonFile.toPath());
        //when
        this.mockMvc.perform(put("/forum/update")
                        .contentType(APPLICATION_JSON)
                        .content(forumToUpdate))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isMap())
                .andExpect(jsonPath("$", aMapWithSize(4)));
        //then
        assertThat(forumRepo.findAll()).hasSize(5);
    }

    @Test
    @WithMockUser(authorities = {"USER"})
    @SqlGroup({
            @Sql(value = "classpath:empty/reset.sql", executionPhase = BEFORE_TEST_METHOD),
            @Sql(value = "classpath:init/forum/forum-data.sql", executionPhase = BEFORE_TEST_METHOD)
    })
    void deleteForum() throws Exception {
        //given
        int id =1;
        //when
        this.mockMvc.perform(delete("/forum/delete/"+id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("Forum with ID: "+id+" was deleted"));
        //then
        assertThat(forumRepo.findAll()).hasSize(4);
    }
}