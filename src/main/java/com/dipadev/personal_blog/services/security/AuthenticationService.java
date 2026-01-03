package com.dipadev.personal_blog.services.security;

import com.dipadev.personal_blog.exceptions.BusinessException;
import com.dipadev.personal_blog.models.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    /**
     * Extract the currently authenticated user from JWT token
     * @return User object from SecurityContext
     * @throws BusinessException if user is not authenticated
     */
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new BusinessException("User is not authenticated");
        }
        
        Object principal = authentication.getPrincipal();
        
        if (!(principal instanceof User)) {
            throw new BusinessException("Invalid authentication principal");
        }
        
        return (User) principal;
    }

    /**
     * Extract the user ID from the currently authenticated user
     * @return User ID
     * @throws BusinessException if user is not authenticated
     */
    public Integer getCurrentUserId() {
        User user = getCurrentUser();
        return user.getId();
    }

    /**
     * Extract the username from the currently authenticated user
     * @return Username
     * @throws BusinessException if user is not authenticated
     */
    public String getCurrentUsername() {
        User user = getCurrentUser();
        return user.getUsername();
    }

    /**
     * Check if user is authenticated
     * @return true if authenticated, false otherwise
     */
    public boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated();
    }
}
