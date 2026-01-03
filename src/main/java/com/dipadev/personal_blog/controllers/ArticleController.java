package com.dipadev.personal_blog.controllers;

import com.dipadev.personal_blog.dtos.ArticleRequestDTO;
import com.dipadev.personal_blog.dtos.ArticleResponseDTO;
import com.dipadev.personal_blog.services.business.ArticleService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/articles")
public class ArticleController {

    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping
    public ResponseEntity<List<ArticleResponseDTO>> getAllArticles() {
        List<ArticleResponseDTO> articles = articleService.getAllArticles();
        return ResponseEntity.ok(articles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArticleResponseDTO> getArticleById(@PathVariable Integer id) {
        ArticleResponseDTO article = articleService.getArticleById(id);
        return ResponseEntity.ok(article);
    }

    @PostMapping
    public ResponseEntity<ArticleResponseDTO> createArticle(@Valid @RequestBody ArticleRequestDTO articleRequest) {
        ArticleResponseDTO createdArticle = articleService.createArticle(articleRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdArticle.id())
                .toUri();

        return ResponseEntity.created(location).body(createdArticle);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ArticleResponseDTO> updateArticle(
            @PathVariable Integer id,
            @Valid @RequestBody ArticleRequestDTO articleRequest) {
        
        ArticleResponseDTO updatedArticle = articleService.updateArticleAsCurrentUser(id, articleRequest);
        return ResponseEntity.ok(updatedArticle);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable Integer id) {
        articleService.deleteArticleAsCurrentUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ArticleResponseDTO>> getArticlesByUserId(@PathVariable Integer userId) {
        List<ArticleResponseDTO> articles = articleService.getArticlesByUserId(userId);
        return ResponseEntity.ok(articles);
    }
}
