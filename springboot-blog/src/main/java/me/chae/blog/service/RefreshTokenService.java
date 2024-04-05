package me.chae.blog.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import me.chae.blog.domain.RefreshToken;
import me.chae.blog.repository.RefreshTokenRepository;

@RequiredArgsConstructor
@Service
public class RefreshTokenService {

	private final RefreshTokenRepository refreshTokenRepository;
	
	public RefreshToken findByRefreshToken(String refreshToken) {
		return refreshTokenRepository.findByRefreshToken(refreshToken).orElseThrow(() -> new IllegalArgumentException("Unexpected token"));
	}
}
