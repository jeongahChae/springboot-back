package me.chae.blog.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import lombok.RequiredArgsConstructor;
import me.chae.blog.domain.Article;
import me.chae.blog.dto.ArticleListViewResponse;
import me.chae.blog.dto.ArticleViewResponse;
import me.chae.blog.service.BlogService;

@RequiredArgsConstructor
@Controller
public class BlogViewController {

	private final BlogService blogService;
	
	@GetMapping("/articles")
	public String getArticles(Model model) {
		List<ArticleListViewResponse> articles = blogService.findAll().stream().map(ArticleListViewResponse::new).toList();
		model.addAttribute("articles", articles);	// 블로그 글 리스트 저장
		
		return "articleList";	//articleList.html라는 뷰 조회
	}
	
	@GetMapping("/articles/{id}")
	public String getArticle(@PathVariable(name = "id") Long id, Model model) {
		Article article = blogService.findById(id);
		model.addAttribute("article", new ArticleViewResponse(article));
		
		return "article";
	}
}
