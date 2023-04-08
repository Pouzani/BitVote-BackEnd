package com.example.demo.users.service;

import com.example.demo.users.exceptions.UserNotFoundException;
import com.example.demo.users.model.User;
import com.example.demo.users.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserService {
    private final UserRepo userRepo;

    @Autowired
    public UserService(UserRepo userRepo){
        this.userRepo = userRepo;
    }

    public List<User> getAllUsers(){
        return userRepo.findAll();
    }

    public List<User> findbyUsername(String username){
        return userRepo.findAllByUsernameContainingIgnoreCase(username).orElseThrow();
    }

    public User updateUser(User user){
        return userRepo.save(user);
    }

    public void deleteUser(Integer id){
       userRepo.deleteById(id);
    }
    public User getUserByUsername(String username){
        return userRepo.findByUsername(username).orElseThrow(()-> new UserNotFoundException("User by username" + username + "was not found"));
    }
}
