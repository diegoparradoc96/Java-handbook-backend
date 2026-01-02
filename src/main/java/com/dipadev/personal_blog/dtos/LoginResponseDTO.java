package com.dipadev.personal_blog.dtos;

public record LoginResponseDTO(
    String token,
    Integer userId,
    String username,
    String type
) {
    public LoginResponseDTO(String token, Integer userId, String username) {
        this(token, userId, username, "Bearer");
    }
}
