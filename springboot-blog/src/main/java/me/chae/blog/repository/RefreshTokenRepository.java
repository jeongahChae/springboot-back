package me.chae.blog.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import me.chae.blog.domain.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
	
	Optional<RefreshToken> findByUserId(Long userId);
	
	Optional<RefreshToken> findByRefreshToken(String refreshToken);

}
