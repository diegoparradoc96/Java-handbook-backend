package com.dipadev.personal_blog.services.business;

import com.dipadev.personal_blog.dtos.LoginRequestDTO;
import com.dipadev.personal_blog.dtos.LoginResponseDTO;
import com.dipadev.personal_blog.dtos.UserRequestDTO;
import com.dipadev.personal_blog.dtos.UserResponseDTO;
import java.util.List;

public interface UserService {
    List<UserResponseDTO> getUsers();
    UserResponseDTO postUser(UserRequestDTO userRequest);
    LoginResponseDTO login(LoginRequestDTO loginRequest);
}
