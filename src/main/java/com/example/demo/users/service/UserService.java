package com.example.demo.users.service;

import com.example.demo.users.model.User;
import com.example.demo.users.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public Optional<List<User>> findbyUsername(String username){
        return userRepo.findAllByUsernameContainingIgnoreCase(username);
    }

    public User updateUser(User user){
        return userRepo.save(user);
    }

    public void deleteUser(Integer id){
       userRepo.deleteById(id);
    }
    public User getUserByUsername(String username){
        return userRepo.findUserByUsername(username);
    }
}
