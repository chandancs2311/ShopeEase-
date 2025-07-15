package com.example.ShopEase.controller;

import com.example.ShopEase.model.User;
import com.example.ShopEase.service.UserService;
import com.example.ShopEase.config.JwtUtil;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    // Register (works for both user and admin)
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        userService.save(user); // default "USER" if role not given
        return ResponseEntity.ok("User registered successfully");
    }

    // Login (for both)
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        String password = payload.get("password");

        Optional<User> userOpt = userService.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (passwordEncoder.matches(password, user.getPassword())) {
                String token = jwtUtil.generateToken(user.getEmail(), user.getRole(), user.getId());


                return ResponseEntity.ok(Map.of(
                        "token", token,
                        "role", user.getRole(),
                        "email", user.getEmail()
                ));
            }
        }

        return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
    }

    // Profile
    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String email = jwtUtil.extractUsername(token);
        User user = userService.getUserByEmail(email);
        return ResponseEntity.ok(user);
    }

    // Admin Profile
    @GetMapping("/admin/profile")
    public ResponseEntity<?> getAdminProfile(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String email = jwtUtil.extractUsername(token);
        User admin = userService.getUserByEmail(email);

        if (!"ADMIN".equalsIgnoreCase(admin.getRole())) {
            return ResponseEntity.status(403).body("Access Denied: Not an Admin");
        }

        Map<String, Object> response = new HashMap<>();
        response.put("admin", admin);
        response.put("allUsers", userService.getAllUsers());

        return ResponseEntity.ok(response);
    }
}


