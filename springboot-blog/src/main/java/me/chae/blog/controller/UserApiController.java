package me.chae.blog.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import me.chae.blog.dto.AddUserRequest;
import me.chae.blog.service.UserService;

@RequiredArgsConstructor
@Controller
public class UserApiController {

	private final UserService userService;
	
	@PostMapping("/user")
	public String signup(AddUserRequest request) {
		userService.save(request);	// 회원 가입 메서드 호출
		return "redirect:/login";	// 회원 가입이 완료된 이후 로그인 페이지로 이동
	}
	
	@GetMapping("/logout")
	public String logout(HttpServletRequest request, HttpServletResponse response) {
		new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
		// 로그아웃 GET요청 > SecurityContextLogoutHandler의 logout() 메서드를 호출해서 로그아웃
		return "redirect:/login";
	}
}
