package com.dipadev.personal_blog.repository;

import com.dipadev.personal_blog.models.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Integer> {
    List<Article> findByUserId(Integer userId);
}
