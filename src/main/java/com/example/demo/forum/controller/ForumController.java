package com.example.demo.forum.controller;

import com.example.demo.exceptions.ApiRequestException;
import com.example.demo.forum.model.Forum;
import com.example.demo.forum.service.ForumService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("${api.endpoint}/forum")
public class ForumController {
    private final ForumService forumService;

    public ForumController(ForumService forumService) {
        this.forumService = forumService;
    }

    @GetMapping("")
    public ResponseEntity<List<Forum>> getAllforums(){
        return ResponseEntity.ok(this.forumService.getAllForums());
    }

    @GetMapping("/exception")
    public ResponseEntity<List<Forum>> getAllforumsExceptin(){
        throw new ApiRequestException(
                "api request exception for forums hh"
        );
    }

    @GetMapping("/search")
    public ResponseEntity<Optional<List<Forum>>> searchForums(@RequestParam(value = "query") String query){
        Optional<List<Forum>> result = this.forumService.findForums(query);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/add")
    public ResponseEntity<Forum> addForum(@RequestBody Forum forum){
        return ResponseEntity.ok(this.forumService.updateForum(forum));
    }

    @PutMapping("/update")
    public ResponseEntity<Forum> updateForum(@RequestBody Forum forum){
        return ResponseEntity.ok(this.forumService.addForum(forum));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteForum(@PathVariable("id") Integer id){
        forumService.deleteForum(id);
        return ResponseEntity.ok("Forum with ID: "+id+" was deleted");
    }
}
