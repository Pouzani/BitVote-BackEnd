package com.example.demo.votes.controller;

import com.example.demo.votes.model.Vote;
import com.example.demo.votes.model.VoteCount;
import com.example.demo.votes.service.VoteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/vote")
public class VoteController {

    private final VoteService voteService;

    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    @GetMapping("/count/{name}/up")
    public ResponseEntity<VoteCount> countUpVotes(@PathVariable("name") String name){
        return ResponseEntity.ok(voteService.countByCoinNameAndTypeUP(name));
    }

    @GetMapping("/count/{name}/down")
    public ResponseEntity<VoteCount> countDownVotes(@PathVariable("name") String name){
        return ResponseEntity.ok(voteService.countByCoinNameAndTypeDOWN(name));
    }

    @PostMapping("/add")
    public ResponseEntity<Vote> addVote(@RequestBody Vote vote){
        Vote newVote = voteService.addVote(vote);
        return new ResponseEntity<>(newVote, HttpStatus.CREATED);
    }
}

