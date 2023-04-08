package com.example.demo.forum.service;

import com.example.demo.forum.model.Forum;
import com.example.demo.forum.repository.ForumRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ForumServiceTest {

    @Mock
    private ForumRepo forumRepo;
    private ForumService underTest;

    @BeforeEach
    void setUp() {
        this.underTest=new ForumService(forumRepo);
    }

    @Test
    void canFindForums() {
        //given
        String search = "test";
        //when
        underTest.findForums(search);
        //then
        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(forumRepo).findAllByUsernameContainingIgnoreCaseOrContentContainingIgnoreCaseOrTitleContainingIgnoreCase(stringArgumentCaptor.capture(),stringArgumentCaptor.capture(),stringArgumentCaptor.capture());

        String result = stringArgumentCaptor.getValue();
        assertEquals(search,result);
    }

    @Test
    void canGetAllForums() {
        //when
        underTest.getAllForums();
        //then
        verify(forumRepo).findAll();
    }

    @Test
    void canAddForum() {
        //given
        Forum forum = new Forum(null,"Test","hello guys i need to know","HELP");
        //when
        underTest.addForum(forum);
        //then
        ArgumentCaptor<Forum> forumArgumentCaptor = ArgumentCaptor.forClass(Forum.class);
        verify(forumRepo).save(forumArgumentCaptor.capture());

        Forum result = forumArgumentCaptor.getValue();
        assertEquals(forum,result);
    }

    @Test
    void canUpdateForum() {
        //given
        Forum forum = new Forum(null,"Test","hello guys i need to know","HELP");
        //when
        underTest.updateForum(forum);
        //then
        ArgumentCaptor<Forum> forumArgumentCaptor = ArgumentCaptor.forClass(Forum.class);
        verify(forumRepo).save(forumArgumentCaptor.capture());

        Forum result = forumArgumentCaptor.getValue();
        assertEquals(forum,result);
    }

    @Test
    void canDeleteForum() {
        //given
        Integer id = 1;
        //when
        underTest.deleteForum(id);
        //then
        ArgumentCaptor<Integer> integerArgumentCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(forumRepo).deleteById(integerArgumentCaptor.capture());

        Integer result = integerArgumentCaptor.getValue();
        assertEquals(id,result);
    }
}