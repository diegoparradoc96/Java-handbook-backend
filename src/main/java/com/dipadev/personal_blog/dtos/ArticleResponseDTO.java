package com.dipadev.personal_blog.dtos;

public record ArticleResponseDTO(
    Integer id,
    String title,
    String body,
    UserResponseDTO user) {}
