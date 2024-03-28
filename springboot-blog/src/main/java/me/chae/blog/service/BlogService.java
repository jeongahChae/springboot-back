package me.chae.blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import me.chae.blog.domain.Article;
import me.chae.blog.dto.AddArticleRequest;
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
}