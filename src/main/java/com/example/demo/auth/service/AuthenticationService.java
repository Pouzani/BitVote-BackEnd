package com.example.demo.auth.service;

import com.example.demo.auth.model.AuthenticationRequest;
import com.example.demo.auth.model.AuthenticationResponse;
import com.example.demo.config.JwtService;
import com.example.demo.users.model.User;
import com.example.demo.users.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserRepo userRepo;
    private final JwtService jwtService;

    @Autowired
    public AuthenticationService(AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, UserRepo userRepo, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.userRepo = userRepo;
        this.jwtService = jwtService;
    }

    public AuthenticationResponse register(User user) {
        var authUser = User.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .age(user.getAge())
                .email(user.getEmail())
                .role(user.getRole())
                .username(user.getUsername())
                .phone(user.getPhone())
                .nationality(user.getNationality())
                .imageUrl(user.getImageUrl())
                .password(passwordEncoder.encode(user.getPassword()))
                .build();
        userRepo.save(authUser);
        var jwtToken = jwtService.generateToken(authUser);
        return AuthenticationResponse
                .builder()
                .token(jwtToken)
                .role(authUser.getRole())
                .username(authUser.getUsername())
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest userData) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userData.getUsername(),userData.getPassword()));
        var user = userRepo.findUserByUsername(userData.getUsername());
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse
                .builder()
                .token(jwtToken)
                .role(user.getRole())
                .username(user.getUsername())
                .build();
    }
}
