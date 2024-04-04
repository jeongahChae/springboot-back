package me.chae.blog.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import me.chae.blog.domain.Article;
import me.chae.blog.dto.AddArticleRequest;
import me.chae.blog.repository.BlogRepository;

@SpringBootTest	// 테스트용 애플리케이션 컨텍스트
@AutoConfigureMockMvc	// MockMvc 생성 및 자동 구성
class BlogApiControllerTest {

	@Autowired
	protected MockMvc mockMvc;
	
	@Autowired
	protected ObjectMapper objectMapper;	// 직렬화(java 객체를 JSON 데이터로 변환), 역직렬화(JSON 데이터를 자바에서 사용하기 위해 java 객체로 변환)를 위한 클래스, jackson 라이브러리 제공
	
	@Autowired
	private WebApplicationContext context;
	
	@Autowired
	BlogRepository blogRepository;
	
	@BeforeEach	//테스트 실행 전 실행하는 메서드
	public void mockMvcSetUp() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
		blogRepository.deleteAll();
	}
	
	@DisplayName("addArticle: 블로그 글 추가에 성공한다.")
	@Test
	public void addArticle() throws Exception {
		
		// given 블로그 글 추가에 필요한 요청 객체 생성
		final String url = "/api/articles";
		final String title = "title";
		final String content = "content";
		final AddArticleRequest userRequest = new AddArticleRequest(title, content);
		
		// 객체 JSON으로 직렬화
		final String requestBody = objectMapper.writeValueAsString(userRequest);	// 객체 > JSON으로 직렬화

		// when 블로그 글 추가 API에 요청을 보냄(요청 타입: JSON)
		ResultActions result = mockMvc.perform(post(url)
													.contentType(MediaType.APPLICATION_JSON_VALUE)
													.content(requestBody));
		
		// Then 응답 코드 201(Created) 확인, 실제 저장된 데이터와 요청 값 비교
		result.andExpect(status().isCreated());
		
		List<Article> articles = blogRepository.findAll();
		
		assertThat(articles.size()).isEqualTo(1);	// 크기가 1인지 검증
		assertThat(articles.get(0).getTitle()).isEqualTo(title);
		assertThat(articles.get(0).getContent()).isEqualTo(content);
		
	}
	
	@DisplayName("findAllArticles: 블로그 글 목록 조회에 성공한다.")
	@Test
	public void findAllArticles() throws Exception {
		
		// given: 블로그 글 저장
		final String url = "/api/articles";
		final String title = "title";
		final String content = "content";
		
		blogRepository.save(Article.builder()
								   .title(title)
								   .content(content)
								   .build());
		
		// when: 목록조회 API 호출
		final ResultActions resultActions = mockMvc.perform(get(url)
																.accept(MediaType.APPLICATION_JSON));
		
		// then: 응답코드 200(OK), 반환받은 값 중 0번째 요소의 content와 title이 저장된 값과 같은지 확인
		resultActions
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].content").value(content))
				.andExpect(jsonPath("$[0].title").value(title));
		
	}

}
