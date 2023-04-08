package com.example.demo.forum.repository;

import com.example.demo.forum.model.Forum;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ForumRepoTest {

    @Autowired
    private ForumRepo underTest;

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void canFindAllByUsernameContainingIgnoreCaseOrContentContainingIgnoreCaseOrTitleContainingIgnoreCase() {
        //given
        String search ="hello";
        Forum forum = new Forum(null,"Test","hello guys i need to know","HELP");
        underTest.save(forum);
        List<Forum> forums = new ArrayList<>();
        forums.add(forum);

        //when
        Optional<List<Forum>> result = underTest.findAllByUsernameContainingIgnoreCaseOrContentContainingIgnoreCaseOrTitleContainingIgnoreCase(search,search,search);
        //then
        assertEquals(Optional.of(forums), result);

    }
}