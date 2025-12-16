package com.dipadev.personal_blog.dtos;

public record UserResponseDTO(
        Integer id,
        String username,
        Byte typeUser
) {}
