package me.chae.blog.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import me.chae.blog.domain.User;

public interface UserRepository extends JpaRepository<User, Long>{
	
	Optional<User> findByEmail(String email);	// 이메일로 사용자 정보 가져옴

}
