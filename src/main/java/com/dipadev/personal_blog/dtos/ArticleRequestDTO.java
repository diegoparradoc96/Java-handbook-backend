package com.dipadev.personal_blog.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ArticleRequestDTO(

    @NotBlank(message = "Title don't be empty")
    @Size(min = 5, max = 100, message = "Title must be between 5 and 100 characters long")
    String title,

    @NotBlank(message = "Body don't be empty")
    String body,

    @NotNull(message = "User ID don't be empty")
    Integer userId
) {}