package com.example.demo.users.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_user",
        uniqueConstraints =
                @UniqueConstraint(columnNames = {"username","email"})
)


public class User {

    @Id
    @GeneratedValue
    private Integer id;
    private String firstName;
    private String lastName;

    @Column(nullable = false)

    @Email(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "email must be valid")
    private String email;
    @NotBlank(message = "username must not be empty")
    @Column(nullable = false)
    private String username;
    @NotBlank(message = "password must not be empty")
    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;
    private String imageUrl;
    private Integer age;
    private String phone;
    private String nationality;
}
