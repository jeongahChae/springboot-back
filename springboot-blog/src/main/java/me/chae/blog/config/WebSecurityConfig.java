package me.chae.blog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import lombok.RequiredArgsConstructor;
import me.chae.blog.service.UserDetailService;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@RequiredArgsConstructor
@Configuration
public class WebSecurityConfig {

	private final UserDetailsService userService;
	
	// 스프링 시큐리티 기능 비활성화
	@Bean
	public WebSecurityCustomizer configure() {
		return (web) -> web.ignoring()
						   .requestMatchers(toH2Console())
						   .requestMatchers("/static/**");
		// 보통 정적 리소스만 시큐리티 사용 비활성화
		// static 하위 경로에 있는 리소스와 h2의 데이터를 확인하는 데 사용하는 h2-console 하위 url을 대상으로 ignoring() 
	}
	
	// 특정 HTTP 요청에 대한 웹 기반 보안 구성(인증, 인가, 로그인, 로그아웃)
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		return http
                .authorizeHttpRequests(requests -> requests		// 인증, 인가 설정: 특정 경로에 대한 액세스 설정
                        .requestMatchers("/login", "/signup", "/user").permitAll()	// requestMatchers: 특정 요청과 일치하는 url에 대한 액세스 설정, permitAll(): 누구나 접근 가능
                        .anyRequest().authenticated())								// anyRequest(): 위 설정 url 이외의 요청에 대한 설정, authenticated(): 별도의 인가는 필요하지 않지만 인증이 성공된 상태여야 접근 가능
                .formLogin(login -> login						// 폼 기반 로그인 설정
                        .loginPage("/login")						// 로그인 페이지 경로 설정
                        .defaultSuccessUrl("/articles"))			// 로그인 완료시 이동할 경로 설정
                .logout(logout -> logout						// 로그아웃 설정
                        .logoutSuccessUrl("/login")					// 로그아웃 완료시 이동할 경로 설정
                        .invalidateHttpSession(true))				// 로그아웃 이후 세션을 전체 삭제할지 여부 설정
                .csrf(csrf -> csrf.disable())					// csrf 비활성화(CSRF 공경 방지를 위해서는 설정이 좋음, 실습을 편리하게 하기 위해 비활성화)
                .build();
	}
	
	// 인증 관리자 관련 설정: 사용자 정보를 가져올 서비스 재정의, 인증방법 등을 설정할 때 사용
	@Bean
	public AuthenticationManager authenticationManager(HttpSecurity http,
			BCryptPasswordEncoder bCryptPasswordEncoder, UserDetailService userDetailService) throws Exception {
		return http
				.getSharedObject(AuthenticationManagerBuilder.class)
				.userDetailsService(userService)	// 사용자 정보를 가져올 서비스 설정, 설정하는 서비스 클래스는 반드시 UserDetailsService를 상속받은 클래스일 것!
				.passwordEncoder(bCryptPasswordEncoder)	// 비밀번호 암호화용 인코더
				.and()
				.build();
	}
	
	// 패스워드 인코더로 사용할 빈 등록
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
