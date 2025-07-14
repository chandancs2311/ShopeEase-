package com.example.ShopEase.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.lang.String;
import java.time.LocalDateTime;

@Entity
@Table(name = "user")
@Data //  Generates getters, setters, toString, equals, hashCode
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    private String role;

    private LocalDateTime createdAt = LocalDateTime.now();
}
