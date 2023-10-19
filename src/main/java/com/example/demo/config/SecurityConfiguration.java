package com.example.demo.config;

import com.example.demo.exceptions.ApiExceptionHandler;
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
                .requestMatchers("/api/v1/forum").permitAll()
                .requestMatchers("/api/v1/forum/search").permitAll()
                .requestMatchers("/api/v1/forum/exception").permitAll()
                .requestMatchers("/api/v1/auth/**").permitAll()
                .requestMatchers("/api/v1/vote/count/{name}/**").permitAll()
                .requestMatchers("/").permitAll()
                .and()
                .authorizeHttpRequests()
                .requestMatchers("/api/v1/users/find/{username}").hasAnyAuthority("ADMIN","USER")
                .requestMatchers("/api/v1/users/update").hasAnyAuthority("ADMIN","USER")
                .requestMatchers("/api/v1/vote/add").hasAnyAuthority("ADMIN","USER")
                .requestMatchers("/api/v1/forum/add").hasAnyAuthority("ADMIN","USER")
                .requestMatchers("/api/v1/forum/update").hasAnyAuthority("ADMIN","USER")
                .requestMatchers("/api/v1/forum/delete").hasAnyAuthority("ADMIN","USER")
                .requestMatchers("/api/v1/users/**").hasAnyAuthority("ADMIN")
                .anyRequest().authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFiler, UsernamePasswordAuthenticationFilter.class)
        ;

        return http.build();
    }
}
