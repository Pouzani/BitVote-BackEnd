package com.example.demo.votes.service;

import com.example.demo.votes.model.Type;
import com.example.demo.votes.model.Vote;
import com.example.demo.votes.model.VoteCount;
import com.example.demo.votes.repository.VoteRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VoteService {
    private final VoteRepo voteRepo;


    public VoteService(VoteRepo voteRepo) {
        this.voteRepo = voteRepo;
    }

    public VoteCount countByCoinNameAndTypeUP(String name){
        long count= voteRepo.countByCoinNameAndType(name, Type.UP);
        return VoteCount.builder().coinName(name).count(count).build();
    }

    public VoteCount countByCoinNameAndTypeDOWN(String name){
        long count= voteRepo.countByCoinNameAndType(name, Type.DOWN);
        return VoteCount.builder().coinName(name).count(count).build();
    }

    public Vote addVote(Vote vote) {
        return voteRepo.save(vote);
    }
}
