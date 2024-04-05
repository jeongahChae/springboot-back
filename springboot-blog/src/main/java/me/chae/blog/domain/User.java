package me.chae.blog.domain;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class User implements UserDetails {	// UserDetails를 상속받아 인증 객체로 사용
	/**
	 * 
	 */
	private static final long serialVersionUID = 6270484514765739318L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", updatable = false)
	private Long id;
	
	@Column(name = "email", nullable = false, unique = true)
	private String email;
	
	@Column(name = "password")
	private String password;
	
	@Builder
	public User(String email, String password, String auth) {
		this.email = email;
		this.password = password;
	}
	
	
	@Override	// 사용자가 가진 권한 목록 권한 반환
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority("user"));
	}
	
	@Override	// 사용자의 패스워드 반환
	public String getPassword() {
		return password;
	}
	
	@Override	// 사용자의 id를 반환(고유한 값)
	public String getUsername() {
		return email;
	}
	
	@Override	// 계정 만료 여부 반환
	public boolean isAccountNonExpired() {
		// 만료되었는지 확인하는 로직(true: 만료되지 않음)
		return true;
	}
	
	@Override	// 계정 잠금 여부 반환
	public boolean isAccountNonLocked() {
		// 계정이 잠금되었는지 확인하는 로직(true: 잠금되지 않았음)
		return true;
	}
	
	@Override	// 패스워드의 만료여부 반환
	public boolean isCredentialsNonExpired() {
		// 패스워드가 만료되었는지 확인하는 로직(true: 만료되지 않았음)
		return true;
	}
	
	@Override	// 계정 사용 가능 여부 반환
	public boolean isEnabled() {
		// 계정이 사용가능한지 확인하는 로직(true: 사용 가능)
		return true;
	}
	
}
