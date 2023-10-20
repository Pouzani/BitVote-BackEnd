package com.example.demo.votes.service;

import com.example.demo.votes.model.Type;
import com.example.demo.votes.model.Vote;
import com.example.demo.votes.model.VoteCount;
import com.example.demo.votes.repository.VoteRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class VoteServiceTest {

    @Mock
    private VoteRepo voteRepo;
    private VoteService underTest;

    @BeforeEach
    void setUp() {
        this.underTest = new VoteService(voteRepo);
    }

    @Test
    void canCountByCoinNameAndTypeUP() {
        //given
        String name = "bitcoin";
        //when  test test test
        underTest.countByCoinNameAndTypeUP(name);
        //then
        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Type> typeArgumentCaptor = ArgumentCaptor.forClass(Type.class);
        verify(voteRepo).countByCoinNameAndType(stringArgumentCaptor.capture(),typeArgumentCaptor.capture());

        String result = stringArgumentCaptor.getValue();
        Type resultType = typeArgumentCaptor.getValue();

        assertEquals(name,result);
        assertEquals(Type.UP,resultType);
    }

    @Test
    void canCountByCoinNameAndTypeDOWN() {
        //given
        String name = "bitcoin";
        //when
        underTest.countByCoinNameAndTypeDOWN(name);
        //then
        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Type> typeArgumentCaptor = ArgumentCaptor.forClass(Type.class);
        verify(voteRepo).countByCoinNameAndType(stringArgumentCaptor.capture(),typeArgumentCaptor.capture());

        String result = stringArgumentCaptor.getValue();
        Type resultType = typeArgumentCaptor.getValue();

        assertEquals(name,result);
        assertEquals(Type.DOWN,resultType);
    }

    @Test
    void canAddVote() {
        //given
        Vote vote = new Vote(null,"bitcoin",Type.UP,1.1);
        //when
        underTest.addVote(vote);
        //then
        ArgumentCaptor<Vote> voteArgumentCaptor = ArgumentCaptor.forClass(Vote.class);
        verify(voteRepo).save(voteArgumentCaptor.capture());

        Vote result = voteArgumentCaptor.getValue();

        assertEquals(vote,result);
    }
}
