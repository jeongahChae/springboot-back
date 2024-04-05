package me.chae.blog.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Map;

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

import me.chae.blog.config.jwt.JwtFactory;
import me.chae.blog.config.jwt.JwtProperties;
import me.chae.blog.domain.RefreshToken;
import me.chae.blog.domain.User;
import me.chae.blog.dto.CreateAccessTokenRequest;
import me.chae.blog.repository.RefreshTokenRepository;
import me.chae.blog.repository.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
class TokenApiControllerTest {
	
	@Autowired
	protected MockMvc mockMvc;
	@Autowired
	protected ObjectMapper objectMapper;
	@Autowired
	private WebApplicationContext context;
	@Autowired
	JwtProperties jwtProperties;
	@Autowired
	UserRepository userRepository;
	@Autowired
	RefreshTokenRepository refreshTokenRepository;
	
	@BeforeEach
	public void mockMvcSetUp() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
		userRepository.deleteAll();
	}
	
	@DisplayName("createNewAccessToken: 새로운 액세스 토큰을 발급한다.")
	@Test
	public void createNewAccessToken() throws Exception {
		
		// given: 테스트 유저 생성, jjwt라이브러리로 리프레시 토큰 생성 후 DB 저장
			// 토큰 생성 API의 요청 본문에 리프레시 토큰을 포함해 요청 객체 생성
		final String url = "/api/token";
		
		User testUser = userRepository.save(User.builder().email("test@te.st").password("test").build());
		
		String refreshToken = JwtFactory.builder().claims(Map.of("id", testUser.getId())).build().createToken(jwtProperties);
		
		refreshTokenRepository.save(new RefreshToken(testUser.getId(), refreshToken));
		
		CreateAccessTokenRequest request = new CreateAccessTokenRequest();
		request.setRefreshToken(refreshToken);
		
		final String requestBody = objectMapper.writeValueAsString(request);
		
		// when: 토큰 추가 API에 요청을 보냄(요청 타입: JSON)
			// given 절에서 미리 만들어둔 객체를 요청 본문으로 보냄
		ResultActions resultActions = mockMvc.perform(post(url)
															.contentType(MediaType.APPLICATION_JSON_VALUE)
															.content(requestBody));
		
		// then: 응답 코드(201, Created), 응답으로 온 액세스 토큰이 비어있지 않은지 확인
		resultActions
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.accessToken").isNotEmpty());
	}
}
