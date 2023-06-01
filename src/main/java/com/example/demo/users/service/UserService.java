package com.example.demo.users.service;


import com.example.demo.exceptions.UserNotFoundException;
import com.example.demo.users.model.User;
import com.example.demo.users.model.UserResponse;
import com.example.demo.users.repository.UserRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class UserService {
    private final UserRepo userRepo;
    private final UserDTOMapper userDTOMapper;


    public UserService(UserRepo userRepo, UserDTOMapper userDTOMapper){
        this.userRepo = userRepo;
        this.userDTOMapper = userDTOMapper;
    }

    public List<UserResponse> getAllUsers(){
        return userRepo.findAll()
                .stream()
                .map(userDTOMapper).collect(Collectors.toList());
    }

    public List<UserResponse> findByUsernameOrEmail(String search){
        return userRepo.findAllByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(search, search).orElse(List.of())
                .stream()
                .map(userDTOMapper).collect(Collectors.toList());
    }

    public User updateUser(User user){
        if (!userRepo.existsById(user.getId())){
            throw new UserNotFoundException("User with id " + user.getId() + " was not found");
        }
        return userRepo.save(user);
    }

    public void deleteUser(Integer id){
        if (!userRepo.existsById(id)){
            throw new UserNotFoundException("User with id " + id + " was not found");
        }
       userRepo.deleteById(id);
    }
    public UserResponse getUserByUsername(String username){
        return userRepo.findByUsername(username).map(userDTOMapper).orElseThrow(() -> new UserNotFoundException("User by username " + username + " was not found"));
    }
}
