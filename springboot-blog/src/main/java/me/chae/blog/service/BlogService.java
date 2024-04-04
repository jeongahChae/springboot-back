package me.chae.blog.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import me.chae.blog.domain.Article;
import me.chae.blog.dto.AddArticleRequest;
import me.chae.blog.dto.UpdateArticleRequest;
import me.chae.blog.repository.BlogRepository;

@RequiredArgsConstructor	// final이 붙거나 @NotNull이 붙은 필드로 생성자 추가(빈을 생성자로 등록)
@Service	// 빈으로 서블릿 컨테이너에 등록
public class BlogService {

	@Autowired
	private BlogRepository blogRepository;
	
	// 블로그 글 추가 메서드
	public Article save(AddArticleRequest request) {
		return blogRepository.save(request.toEntity());
		// save(): JpaRepository에서 지원하는 저장 메서드
	}
	
	// 블로그 글 목록 조회
	public List<Article> findAll() {
		return blogRepository.findAll();	// JPA 지원 메서드
	}
	
	// 블로그 글 조회
	public Article findById(long id) {
		return blogRepository.findById(id)
							 .orElseThrow(() -> new IllegalArgumentException("not found: " + id));
	}
	
	// 블로그 글 삭제
	public void delete(long id) {
		blogRepository.deleteById(id);
	}

	// 블로그 글 수정
	@Transactional
	public Article update(long id, UpdateArticleRequest request) {
		Article article = blogRepository.findById(id)
										.orElseThrow(() -> new IllegalArgumentException("not found: " + id));
		
		article.update(request.getTitle(), request.getContent());
		
		return article;
	}
}
