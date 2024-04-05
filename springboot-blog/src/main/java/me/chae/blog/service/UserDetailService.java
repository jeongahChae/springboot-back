package me.chae.blog.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import me.chae.blog.repository.UserRepository;

@RequiredArgsConstructor
@Service
public class UserDetailService implements UserDetailsService {	// 스프링 시큐리티에서 사용자 정보를 가져오는 인터페이스
	
	private final UserRepository userRepository;
	
	@Override	// 사용자 이름(email)으로 사용자의 정보를 가져오는 메서드
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		return userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException(email));
	}

}
