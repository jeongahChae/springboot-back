package me.chae.blog.config.jwt;

import java.time.Duration;
import java.util.Date;
import java.util.Map;

import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import static java.util.Collections.emptyMap;

import lombok.Builder;
import lombok.Getter;
import me.chae.blog.config.jwt.JwtProperties;

@Getter
public class JwtFactory {
	// JWT 토큰 서비스를 테스트하는데 사용할 모킹(mocking)용 객체
	// mocking: 테스트를 실행할 때 객체를 대신하는 가짜 객체

	private String subject = "te@s.t";
	private Date issuedAt = new Date();
	private Date expiration = new Date(new Date().getTime() + Duration.ofDays(14).toMillis());
	private Map<String, Object> claims = emptyMap();
	
	// 빌더 패턴을 사용해 설정이 필요한 데이터만 선택 설정 > 빌더 패턴을 사용하지 않으면 필드 기본값을 사용함
	@Builder
	public JwtFactory(String subject, Date issuedAt, Date expiration, Map<String, Object> claims) {
		this.subject = subject != null ? subject : this.subject;
		this.issuedAt = issuedAt != null ? issuedAt : this.issuedAt;
		this.expiration = expiration != null ? expiration : this.expiration;
		this.claims = claims != null ? claims : this.claims;
	}
	
	public static JwtFactory withDefaultValues() {
		return JwtFactory.builder().build();
	}
	
	public String createToken(JwtProperties jwtProperties	) {
		return Jwts.builder()
				   .setSubject(subject)
				   .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
				   .setIssuer(jwtProperties.getIssuer())
				   .setIssuedAt(issuedAt)
				   .setExpiration(expiration)
				   .addClaims(claims)
				   .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())
				   .compact();
	}
}
