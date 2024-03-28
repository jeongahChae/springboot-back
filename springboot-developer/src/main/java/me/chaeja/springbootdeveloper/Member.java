package me.chaeja.springbootdeveloper;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)	//기본 생성자
@AllArgsConstructor
@Getter
@Entity
public class Member {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)	//기본키 생성 방식 결정, IDENTITY: 기본키 생성을 DB에 위임
	@Column(name = "id", updatable = false)
	private Long id;
	
	@Column(name = "name", nullable = false)
	private String name;
}
