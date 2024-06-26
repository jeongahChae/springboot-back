package me.chae.blog.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;import me.chae.blog.dto.CreateAccessTokenRequest;
import me.chae.blog.dto.CreateAccessTokenResponse;
import me.chae.blog.service.TokenService;

@RequiredArgsConstructor
@RestController
public class TokenApiController {

	private final TokenService tokenService;
	
	@PostMapping("/api/token")
	public ResponseEntity<CreateAccessTokenResponse> createNewAccessToken(@RequestBody CreateAccessTokenRequest request) {
		String newAccessToken = tokenService.createNewAccessToken(request.getRefreshToken());
		
		return ResponseEntity.status(HttpStatus.CREATED).body(new CreateAccessTokenResponse(newAccessToken));
	}
}
