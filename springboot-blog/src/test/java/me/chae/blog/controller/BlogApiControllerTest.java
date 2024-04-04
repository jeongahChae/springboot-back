package me.chae.blog.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
import me.chae.blog.dto.UpdateArticleRequest;
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

	@DisplayName("findArticle: 블로그 글 조회에 성공한다")
	@Test
	public void findArticle() throws Exception {

		// given: 블로그 글 저장
		final String url = "/api/articles/{id}";
		final String title = "title";
		final String content = "content";
		
		Article savedArticle = blogRepository.save(Article.builder().title(title).content(content).build());

		//when: 저장한 블로그 글의 id값으로 API 호출
		final ResultActions resultActions = mockMvc.perform(get(url, savedArticle.getId()));
		
		//then: 응답코드(200), 반환받은 content와 title이 저장된 값과 같은지 확인
		resultActions
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.content").value(content))
				.andExpect(jsonPath("$.title").value(title));
						
	}
	
	@DisplayName("deleteArticle: 블로그 글 삭제에 성공한다.")
	@Test
	public void deleteArticle() throws Exception {
		
		// given: 블로그 글 저장
		final String url = "/api/articles/{id}";
		final String title = "title";
		final String content = "content";
		
		Article savedArticle = blogRepository.save(Article.builder().title(title).content(content).build());
		
		// when: 저장한 블로그 글의 id값으로 삭제 API 호출
		mockMvc.perform(delete(url, savedArticle.getId()))
		.andExpect(status().isOk());
		
		// then: 응답코드(200),블로그 글 리스트를 전체 조회해 조회한 배열 크기가 0인지 확인
		List<Article> articles = blogRepository.findAll();
		assertThat(articles).isEmpty();
		
	}
	
	@DisplayName("updateArticle: 블로그 글 수정에 성공한다.")
	@Test
	public void updateArticle() throws Exception {
		
		// given: 블로그 글 저장, 글 수정에 필요한 요청 객체 생성
		final String url = "/api/articles/{id}";
		final String title = "title";
		final String content = "content";
		
		Article savedArticle = blogRepository.save(Article.builder().title(title).content(content).build());
		
		final String newTitle = "new Title";
		final String newContent = "new content";
		
		UpdateArticleRequest request = new UpdateArticleRequest(newTitle, newContent);
		
		// when: update API로 수정 요청 보냄(요청 타입: JSON, given에서 만든 객체를 요청 본문으로 함께 보냄)
		ResultActions result = mockMvc.perform(put(url, savedArticle.getId())
												.contentType(MediaType.APPLICATION_JSON_VALUE)
												.content(objectMapper.writeValueAsString(request)));
		
		// then: 응답 코드(200), 블로그 글 id로 조회한 후 값이 수정되었는지 확인
		result.andExpect(status().isOk());
		
		Article article = blogRepository.findById(savedArticle.getId()).get();
		
		assertThat(article.getTitle()).isEqualTo(newTitle);
		assertThat(article.getContent()).isEqualTo(newContent);
		
	}
	
	//BlogService에 대해서도 테스트 코드를 작성하는 것이 좋음
}
