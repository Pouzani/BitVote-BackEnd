package com.example.demo.users.controller;

import com.example.demo.users.model.User;
import com.example.demo.users.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("")
    public ResponseEntity<List<User>> getAllUsers(){
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/find/{username}")
    public ResponseEntity<User> getUserById(@PathVariable("username") String username){
        User user = userService.getUserByUsername(username);
        return ResponseEntity.ok(user);
    }


    @PutMapping("/update")
    public ResponseEntity<User> updateUser(@RequestBody User user){
        User updatedUser = userService.updateUser(user);
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping("/search")
    public ResponseEntity<List<User>> searchUser(@RequestParam(value = "query") String query){
        List<User> users = userService.findByUsernameOrEmail(query);
        return ResponseEntity.ok(users);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") Integer id){
        userService.deleteUser(id);
        return ResponseEntity.ok("User with ID: "+id+" was deleted");
    }

}
