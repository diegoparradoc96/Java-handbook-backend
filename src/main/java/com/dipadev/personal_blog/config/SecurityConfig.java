package com.dipadev.personal_blog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF (TODO: Enable in production with proper JWT token handling)
                .csrf(csrf -> csrf.disable())
                
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints
                        .requestMatchers("/users").permitAll()                        // User registration
                        .requestMatchers("/users/login").permitAll()                 // User login
                        .requestMatchers("/articles").permitAll()                   // View all articles
                        .requestMatchers("/articles/{id}").permitAll()              // View specific article
                        .requestMatchers("/articles/user/{userId}").permitAll()     // View user's articles
                        
                        // Protected endpoints - require authentication
                        .requestMatchers(HttpMethod.POST, "/articles").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/articles/{id}").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/articles/{id}").authenticated()
                        
                        // All other requests require authentication
                        .anyRequest().authenticated()
                )
                
                // Add JWT filter before UsernamePasswordAuthenticationFilter
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
                
        return http.build();
    }
}

