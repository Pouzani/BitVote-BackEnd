package com.example.demo.votes.controller;

import com.example.demo.votes.repository.VoteRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.assertj.core.api.Assertions.*;


@SpringBootTest
@AutoConfigureMockMvc
class VoteControllerTest {

    @Autowired
    private VoteRepo voteRepo;

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
            @Sql(value = "classpath:init/vote/vote-data.sql", executionPhase = BEFORE_TEST_METHOD)
    })
    void countUpVotes() throws Exception {
        //given
        String name = "bitcoin";
        //then
        this.mockMvc.perform(get("/vote/count/"+name+"/up"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$").isMap())
                .andExpect(jsonPath("$.coinName").value(name))
                .andExpect(jsonPath("$.count").value(2));
    }

    @Test
    @SqlGroup({
            @Sql(value = "classpath:empty/reset.sql", executionPhase = BEFORE_TEST_METHOD),
            @Sql(value = "classpath:init/vote/vote-data.sql", executionPhase = BEFORE_TEST_METHOD)
    })
    void countDownVotes() throws Exception {
        //given
        String name = "etherium";
        //then
        this.mockMvc.perform(get("/vote/count/"+name+"/down"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$").isMap())
                .andExpect(jsonPath("$.coinName").value(name))
                .andExpect(jsonPath("$.count").value(1));
    }

    @Test
    @SqlGroup({
            @Sql(value = "classpath:empty/reset.sql", executionPhase = BEFORE_TEST_METHOD),
            @Sql(value = "classpath:init/vote/vote-data.sql", executionPhase = BEFORE_TEST_METHOD)
    })
    @WithMockUser(authorities ={"USER"})
    void addVote() throws Exception {
        //given
        final File jsonFile = new ClassPathResource("init/vote/vote.json").getFile();
        final String voteToAdd = Files.readString(jsonFile.toPath());
        //when
        this.mockMvc.perform(post("/vote/add")
                .contentType(APPLICATION_JSON)
                .content(voteToAdd))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").isMap())
                .andExpect(jsonPath("$.coinName").value("binance"));

        //then
        assertThat(voteRepo.findAll()).hasSize(4);
    }
}