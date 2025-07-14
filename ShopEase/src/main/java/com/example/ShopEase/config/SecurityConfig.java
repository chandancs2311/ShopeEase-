package com.example.ShopEase.config;

import com.example.ShopEase.exception.CustomAccessDeniedHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;
    private final UserDetailsService userDetailsService;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth

                                // Login and Register
                                .requestMatchers("/api/user/login", "/api/user/register").permitAll()

// Profile
                                .requestMatchers("/api/user/profile").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
                                .requestMatchers("/api/user/admin/profile").hasAuthority("ROLE_ADMIN")

// Products and Categories
                                .requestMatchers("/api/products/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                                .requestMatchers("/api/categories/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")

// 🟡 Orders - MUST be ABOVE `/api/user/**` and `/api/admin/**`
                                .requestMatchers("/api/user/orders/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                                .requestMatchers("/api/admin/orders/**").hasAuthority("ROLE_ADMIN")

// Cart
                                .requestMatchers("/api/users/cart/**").hasAuthority("ROLE_USER")

// 🟡 General admin and user endpoints - MUST BE LAST
                                .requestMatchers("/api/admin/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                                .requestMatchers("/api/user/**").hasAuthority("ROLE_USER")

// Final catch-all

                                .anyRequest().authenticated()
                )
                .exceptionHandling(exception ->
                        exception.accessDeniedHandler(customAccessDeniedHandler)
                )
                .sessionManagement(sess ->
                        sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }


    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(userDetailsService);
        auth.setPasswordEncoder(passwordEncoder());
        return auth;
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }


}
