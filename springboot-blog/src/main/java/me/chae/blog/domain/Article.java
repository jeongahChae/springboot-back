package me.chae.blog.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity	// 엔티티 지정
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Article {

	@Id	//id필드를 기본키로 지정
	@GeneratedValue(strategy = GenerationType.IDENTITY)	// 기본키를 자동으로 1씩 증가
	@Column(name = "id", updatable = false)
	private Long id;
	
	@Column(name = "title", nullable = false)	// title이라는 not null 컬럼과 매핑
	private String title;
	
	@Column(name = "content", nullable = false)
	private String content;
	
	@Builder	// 롬복에서 지원하는 빌더패턴으로 객체 생성, 생성자 위에 입력
	public Article(String title, String content) {
		this.title = title;
		this.content = content;
	}
//	[빌더 패턴]
//	객체를 유연하고 직관적으로 생성하는 디자인 패턴
//	어느 필드에 어떤 값이 들어가는지 명시적으로 파악
//	객체 생성 코드의 가독성이 높아짐
//	어노테이션 사용 > 빌더 패턴을 사용하기 위한 코드를 자동으로 생성하여 간편하게 빌더패턴을 사용할 수 있음

	public void update(String title, String content) {
		this.title = title;
		this.content = content;
	}
}
