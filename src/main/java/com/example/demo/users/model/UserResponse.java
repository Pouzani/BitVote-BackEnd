package com.example.demo.users.model;

public record UserResponse(Integer id,String username,String email,Role role, String phone, String imageUrl, String firstName, String lastName, Integer age, String nationality) { }
