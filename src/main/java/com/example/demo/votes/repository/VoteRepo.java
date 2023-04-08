package com.example.demo.votes.repository;


import com.example.demo.votes.model.Type;
import com.example.demo.votes.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;



public interface VoteRepo extends JpaRepository<Vote, Long> {
    long countByCoinNameAndType(String name, Type type);
}
