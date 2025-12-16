package com.dipadev.personal_blog.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

public record UserRequestDTO(

        @NotBlank(message = "Username don't be empty")
        @Size(min = 4, max = 20, message = "Username must be between 4 and 20 characters long")
        String username,

        @NotBlank(message = "The password is required")
        @Size(min = 8, message = "The password must be minimum 8 characters")
        String password
) {}
