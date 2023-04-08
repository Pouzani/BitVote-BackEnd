package com.example.demo.config;

import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthFiler;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf()
                .disable()
                .authorizeHttpRequests()
                .requestMatchers("/forum").permitAll()
                .requestMatchers("/forum/search").permitAll()
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers("/vote").permitAll()
                .and()
                .authorizeHttpRequests()
                .requestMatchers("/users/**").hasAnyRole("ADMIN")
                .requestMatchers("/users/update").hasAnyRole("ADMIN","USER")
                .requestMatchers("/vote/add").hasAnyRole("ADMIN","USER")
                .requestMatchers("/forum/add").hasAnyRole("ADMIN","USER")
                .requestMatchers("/forum/update").hasAnyRole("ADMIN","USER")
                .requestMatchers("/forum/delete").hasAnyRole("ADMIN","USER")
                .anyRequest().authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFiler, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
