package com.example.ShopEase.service;

import com.example.ShopEase.model.User;
import com.example.ShopEase.repository.UserRepository;
import com.example.ShopEase.config.JwtUtil;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    // Register new user (default role = USER)
    public void save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("USER"); // default role
        }
        userRepository.save(user);
    }

    //  Login and generate JWT if valid
    public Map<String, Object> login(String email, String rawPassword) {
        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isPresent()) {
            User user = userOpt.get();

            if (passwordEncoder.matches(rawPassword, user.getPassword())) {
                String token = JwtUtil.generateToken(email, user.getRole());

                Map<String, Object> response = new HashMap<>();
                response.put("token", token);
                response.put("role", user.getRole());
                response.put("email", email);
                return response;
            }
        }
        throw new RuntimeException("Invalid credentials");
    }

    //  Get all users (admin-only)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    //  Get a user by email (for profile fetch)
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    //  (Optional) Simple check for login validation
    public boolean isValidUser(String email, String rawPassword) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        return userOpt.isPresent() &&
                passwordEncoder.matches(rawPassword, userOpt.get().getPassword());
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

}
