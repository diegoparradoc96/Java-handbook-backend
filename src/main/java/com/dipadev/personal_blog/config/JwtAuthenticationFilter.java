package com.dipadev.personal_blog.config;

import com.dipadev.personal_blog.repository.UserRepository;
import com.dipadev.personal_blog.services.security.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    public JwtAuthenticationFilter(JwtService jwtService, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        // 1. Extract token from Authorization header
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;

        // Check if header is present and starts with "Bearer "
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2. Extract JWT token (remove "Bearer " prefix)
        jwt = authHeader.substring(7);

        try {
            // 3. Extract username from token
            username = jwtService.extractUsername(jwt);

            // 4. If username is extracted and no authentication is set
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // 5. Load user details from database
                UserDetails userDetails = userRepository.findByUsername(username)
                        .orElse(null);

                // 6. Validate token against user details
                if (userDetails != null && jwtService.isTokenValid(jwt, userDetails)) {
                    // 7. Create authentication token
                    UsernamePasswordAuthenticationToken authToken = 
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // 8. Set authentication in context
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (Exception e) {
            // Token is invalid or expired, continue without authentication
            logger.error("Cannot validate JWT token: " + e.getMessage());
        }

        // 9. Continue filter chain
        filterChain.doFilter(request, response);
    }
}
