package me.chaeja.springbootdeveloper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.AfterEach;
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

@SpringBootTest			//테스트용 애플리케이션 컨텍스트 생성: 메인 앱 클래스의 @SpringBootApplication 클래스를 찾고 그 클래스에 포함된 빈을 찾은 다음 테스트용 앱 컨텍스트를 만듦
@AutoConfigureMockMvc	//MockMVC 생성 및 자동 구성(MockMvc: 앱을 서버에 배포하지 않고도 테스트용 MVC 환경을 만들어 요청, 전송, 응답 기능을 제공하는 유틸리티 클래스 = 컨트롤러 테스트 클래스)
class TestControllerTest {

	@Autowired
	protected MockMvc mockMvc;
	
	@Autowired
	private WebApplicationContext context;
	
	@Autowired
	private MemberRepository memberRepository;
	
	@BeforeEach	//테스트 실행 전 실행하는 메서드
	public void mockMvcSetUp() {
		//테스트 실행 전 MockMvc를 설정
		this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
	}
	@AfterEach	//테스트 실행 후 실행하는 메서드
	public void cleanUp() {
		//테스트 실행 후 member테이블의 데이터 모두 삭제
		memberRepository.deleteAll();
	}
	
	//↓ 여기서부터 TestController 테스트 코드
	@DisplayName("getAllMembers: 아티클 조회에 성공")
	@Test
	public void getAllMembers() throws Exception {
		//given: 테스트 준비, 멤버 저장
		final String url = "/test";
		Member saveMember = memberRepository.save(new Member(1L, "홍길동"));
		
		//when: 테스트 실제 진행, 리스트 조회하는 API 호출
		final ResultActions result = mockMvc.perform(get(url)	//perform(): 요청 전송 메서드, 결과는 ResultActions 객체를 받음
											.accept(MediaType.APPLICATION_JSON));	//accept(): 요청을 보낼 때 무슨 타입으로 응답 받을지 결정하는 메서드(여기서는 json으로 받겠다)
		
		//then: 테스트 결과 검증, 응답코드가 200(OK)이고, 반환 받은 값 중 0번째 요소의 id와 name이 저장된 값과 같은지 확인
		result
			.andExpect(status().isOk())	//andExpect(): 메서드 응답 검증
			//응답의 0번째 값이 DB에 저장한 값과 같은지 확인
			.andExpect(jsonPath("$[0].id").value(saveMember.getId()))	//jsonPath("$[0].${필드명}"): JSON 응답값을 가져오는 역할을 하는 메서드
			.andExpect(jsonPath("$[0].name").value(saveMember.getName()));
	}

}
