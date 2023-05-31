package com.example.demo.forum.service;

import com.example.demo.forum.model.Forum;
import com.example.demo.forum.repository.ForumRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ForumService {
    private final ForumRepo forumRepo;


    public ForumService(ForumRepo forumRepo){
        this.forumRepo=forumRepo;
    }

    public Optional<List<Forum>> findForums(String search){
        return this.forumRepo.findAllByUsernameContainingIgnoreCaseOrContentContainingIgnoreCaseOrTitleContainingIgnoreCase(search, search,search);
    }

    public List<Forum> getAllForums(){
        return this.forumRepo.findAll();
    }

    public Forum addForum(Forum forum){
        return this.forumRepo.save(forum);
    }

    public Forum updateForum(Forum forum){
        return this.forumRepo.save(forum);
    }

    public void deleteForum(Integer id){
        this.forumRepo.deleteById(id);
    }
}

