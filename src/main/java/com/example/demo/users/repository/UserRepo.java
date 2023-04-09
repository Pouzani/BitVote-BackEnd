package com.example.demo.users.repository;


import com.example.demo.users.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepo extends JpaRepository<User,Integer> {
    Optional<List<User>> findAllByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(String search1,String search2);
    Optional<User> findByUsername(String username);
    boolean existsUserByUsername(String username);
}
