package com.example.demo.auth.controller;

import com.example.demo.auth.model.AuthenticationRequest;
import com.example.demo.auth.model.AuthenticationResponse;
import com.example.demo.auth.service.AuthenticationService;
import com.example.demo.users.model.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@Valid @RequestBody User user){
        return new ResponseEntity<>(this.authenticationService.register(user), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@Valid @RequestBody AuthenticationRequest userData){
        return ResponseEntity.ok(this.authenticationService.authenticate(userData));
    }
}
