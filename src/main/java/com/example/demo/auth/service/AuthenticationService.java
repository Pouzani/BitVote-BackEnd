package com.example.demo.auth.service;

import com.example.demo.auth.model.AuthenticationRequest;
import com.example.demo.auth.model.AuthenticationResponse;
import com.example.demo.config.JwtService;
import com.example.demo.exceptions.UserNotFoundException;
import com.example.demo.users.model.User;
import com.example.demo.users.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserRepo userRepo;
    private final JwtService jwtService;


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

    public AuthenticationResponse authenticate(@NotNull AuthenticationRequest userData) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userData.getUsername(),userData.getPassword()));
        if (!userRepo.existsUserByUsername(userData.getUsername())){
            {
                throw new UserNotFoundException("User by username " + userData.getUsername() + " was not found");
            }
        }
        var user = userRepo.findByUsername(userData.getUsername()).orElse(new User());
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse
                .builder()
                .token(jwtToken)
                .role(user.getRole())
                .username(user.getUsername())
                .build();
    }
}
