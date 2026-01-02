package com.dipadev.personal_blog.services.business;

import com.dipadev.personal_blog.dtos.ArticleRequestDTO;
import com.dipadev.personal_blog.dtos.ArticleResponseDTO;
import com.dipadev.personal_blog.exceptions.BusinessException;
import com.dipadev.personal_blog.mappers.ArticleMapper;
import com.dipadev.personal_blog.models.Article;
import com.dipadev.personal_blog.models.User;
import com.dipadev.personal_blog.repository.ArticleRepository;
import com.dipadev.personal_blog.repository.UserRepository;
import com.dipadev.personal_blog.services.security.AuthenticationService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final ArticleMapper articleMapper;
    private final AuthenticationService authenticationService;

    public ArticleServiceImpl(ArticleRepository articleRepository, 
                            UserRepository userRepository, 
                            ArticleMapper articleMapper,
                            AuthenticationService authenticationService) {
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
        this.articleMapper = articleMapper;
        this.authenticationService = authenticationService;
    }

    @Override
    public List<ArticleResponseDTO> getAllArticles() {
        return articleRepository.findAll()
                .stream()
                .map(articleMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ArticleResponseDTO getArticleById(Integer id) {
        if (id == null || id <= 0) {
            throw new BusinessException("Article ID must be valid");
        }
        
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Article not found with id: " + id));
        
        return articleMapper.toResponse(article);
    }

    @Override
    public ArticleResponseDTO createArticle(ArticleRequestDTO articleRequest) {
        // 1. Validate DTO
        if (articleRequest == null) {
            throw new BusinessException("Article request cannot be null");
        }
        
        if (articleRequest.userId() == null || articleRequest.userId() <= 0) {
            throw new BusinessException("User ID must be valid");
        }

        // 2. Check if user exists (SECURITY: prevent creating articles for non-existent users)
        User user = userRepository.findById(articleRequest.userId())
                .orElseThrow(() -> new BusinessException("User not found with id: " + articleRequest.userId()));

        // 3. Convert DTO to entity
        Article article = articleMapper.toEntity(articleRequest);
        
        // 4. Set the user (SECURITY: prevent user from assigning article to another user)
        article.setUser(user);

        // 5. Save
        Article savedArticle = articleRepository.save(article);

        // 6. Return DTO response
        return articleMapper.toResponse(savedArticle);
    }

    @Override
    public ArticleResponseDTO updateArticleAsCurrentUser(Integer id, ArticleRequestDTO articleRequest) {
        Integer userId = authenticationService.getCurrentUserId();
        return updateArticle(id, articleRequest, userId);
    }

    
    public ArticleResponseDTO updateArticle(Integer id, ArticleRequestDTO articleRequest, Integer userId) {
        if (id == null || id <= 0) {
            throw new BusinessException("Article ID must be valid");
        }
        
        if (articleRequest == null) {
            throw new BusinessException("Article request cannot be null");
        }
        
        if (userId == null || userId <= 0) {
            throw new BusinessException("User ID must be valid");
        }

        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Article not found with id: " + id));

        // SECURITY: Verify that the user owns this article
        if (!article.getUser().getId().equals(userId)) {
            throw new BusinessException("You don't have permission to update this article");
        }

        // Update fields
        article.setTitle(articleRequest.title());
        article.setBody(articleRequest.body());
        
        // Note: User cannot be changed in an update (SECURITY: prevent user reassignment)

        Article updatedArticle = articleRepository.save(article);
        return articleMapper.toResponse(updatedArticle);
    }

    @Override
    public void deleteArticleAsCurrentUser(Integer id) {
        Integer userId = authenticationService.getCurrentUserId();
        deleteArticle(id, userId);
    }

    @Override
    public void deleteArticle(Integer id, Integer userId) {
        if (id == null || id <= 0) {
            throw new BusinessException("Article ID must be valid");
        }
        
        if (userId == null || userId <= 0) {
            throw new BusinessException("User ID must be valid");
        }
        
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Article not found with id: " + id));
        
        // SECURITY: Verify that the user owns this article
        if (!article.getUser().getId().equals(userId)) {
            throw new BusinessException("You don't have permission to delete this article");
        }
        
        articleRepository.delete(article);
    }

    @Override
    public List<ArticleResponseDTO> getArticlesByUserId(Integer userId) {
        if (userId == null || userId <= 0) {
            throw new BusinessException("User ID must be valid");
        }
        
        // Verify user exists
        if (!userRepository.existsById(userId)) {
            throw new BusinessException("User not found with id: " + userId);
        }
        
        return articleRepository.findByUserId(userId)
                .stream()
                .map(articleMapper::toResponse)
                .collect(Collectors.toList());
    }
}
