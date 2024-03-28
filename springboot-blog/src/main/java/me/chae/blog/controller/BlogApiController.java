package me.chae.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import me.chae.blog.domain.Article;
import me.chae.blog.dto.AddArticleRequest;
import me.chae.blog.service.BlogService;

@RequiredArgsConstructor
@RestController	// HTTP ResponseBody 에 객체데이터를 JSON형식으로 반환하는 컨트롤러
public class BlogApiController {
	
	@Autowired
	private BlogService blogService;
	
	// HTTP메서드가 POST일 때 전달받은 URL과 동일하면 메서드로 매핑
	@PostMapping("/api/articles")
	public ResponseEntity<Article> addArticle(@RequestBody AddArticleRequest request) {
		// @RequestBody로 요청 본문 값 매핑
		
		Article savedArticle = blogService.save(request);
		return ResponseEntity.status(HttpStatus.CREATED)	// 201
							 .body(savedArticle);
		// 요청한 자원이 성공적으로 생성되었으며 저장된 블로그 글 정보를 응답 객체에 담아 전송
	}
}
