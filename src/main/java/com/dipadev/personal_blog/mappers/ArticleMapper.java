package com.dipadev.personal_blog.mappers;

import com.dipadev.personal_blog.dtos.ArticleRequestDTO;
import com.dipadev.personal_blog.dtos.ArticleResponseDTO;
import com.dipadev.personal_blog.models.Article;
import org.springframework.stereotype.Component;

@Component
public class ArticleMapper {
    
    public ArticleResponseDTO toResponse(Article article) {
        if (article == null) {
            return null;
        }

        return new ArticleResponseDTO(
                article.getId(),
                article.getTitle(),
                article.getBody(),
                new UserMapper().toResponse(article.getUser())
        );
    }

    public Article toEntity(ArticleRequestDTO request) {
        if (request == null) {
            return null;
        }

        return new Article(            
                request.title(),
                request.body(),
                null  // User será asignado por el servicio
        );
    }

}
