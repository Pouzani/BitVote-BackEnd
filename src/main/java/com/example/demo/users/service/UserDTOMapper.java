package com.example.demo.users.service;

import com.example.demo.users.model.User;
import com.example.demo.users.model.UserResponse;
import org.springframework.stereotype.Service;

import java.util.function.Function;



@Service
public class UserDTOMapper implements Function<User, UserResponse> {
    @Override
    public UserResponse apply(User user) {
        return new UserResponse(user.getId(), user.getUsername(),user.getEmail(),user.getRole(), user.getPhone(),user.getImageUrl(), user.getFirstName(), user.getLastName(), user.getAge(), user.getNationality());
    }
}
