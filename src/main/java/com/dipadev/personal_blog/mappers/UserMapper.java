package com.dipadev.personal_blog.mappers;

import com.dipadev.personal_blog.dtos.UserRequestDTO;
import com.dipadev.personal_blog.dtos.UserResponseDTO;
import com.dipadev.personal_blog.models.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponseDTO toResponse(User user) {
        if (user == null) {
            return null;
        }

        return new UserResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getTypeUser()
        );
    }

    public User toEntity(UserRequestDTO request) {
        if (request == null) {
            return null;
        }

        return new User(
                request.username(),
                request.password()
        );
    }

}
