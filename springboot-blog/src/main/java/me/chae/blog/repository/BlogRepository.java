package me.chae.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import me.chae.blog.domain.Article;

public interface BlogRepository extends JpaRepository<Article, Long>{

}
