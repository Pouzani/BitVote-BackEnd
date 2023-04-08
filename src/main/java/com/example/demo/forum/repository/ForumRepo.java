package com.example.demo.forum.repository;

import com.example.demo.forum.model.Forum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ForumRepo extends JpaRepository<Forum,Integer> {
    Optional<List<Forum>> findAllByUsernameContainingIgnoreCaseOrContentContainingIgnoreCaseOrTitleContainingIgnoreCase(String username,String content,String title);
}
