package me.chae.blog.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import me.chae.blog.domain.User;
import me.chae.blog.dto.AddUserRequest;
import me.chae.blog.repository.UserRepository;

@RequiredArgsConstructor
@Service
public class UserService {

	private final UserRepository userRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	
	public Long save(AddUserRequest dto) {
		return userRepository.save(User.builder()
									   .email(dto.getEmail())
									   .password(bCryptPasswordEncoder.encode(dto.getPassword()))
									   .build()).getId();
	}
	
	public User findById(Long userId) {
		return userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
	}
}
