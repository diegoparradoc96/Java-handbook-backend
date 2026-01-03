package com.dipadev.personal_blog.services.business;

import com.dipadev.personal_blog.dtos.ArticleRequestDTO;
import com.dipadev.personal_blog.dtos.ArticleResponseDTO;

import java.util.List;

public interface ArticleService {
    List<ArticleResponseDTO> getAllArticles();
    
    ArticleResponseDTO getArticleById(Integer id);
    
    ArticleResponseDTO createArticle(ArticleRequestDTO articleRequest);
        // Update/Delete with userId extracted from authenticated user
    ArticleResponseDTO updateArticleAsCurrentUser(Integer id, ArticleRequestDTO articleRequest);
    
    void deleteArticleAsCurrentUser(Integer id);
    
    // Legacy methods with explicit userId (for backward compatibility)    ArticleResponseDTO updateArticle(Integer id, ArticleRequestDTO articleRequest);
    
    void deleteArticle(Integer id, Integer userId);
    
    List<ArticleResponseDTO> getArticlesByUserId(Integer userId);
}
