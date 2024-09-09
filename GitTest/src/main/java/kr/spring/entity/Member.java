package kr.spring.entity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

import lombok.Data;

@Entity		// Database Table 생성
@Data
public class Member {
	
	// 인증 : 이 사람의 회원이 존재하는지 검증
	@Id
	private String username;	// Spring Security에서는 id가 아닌 username으로 저장해줘야 한다.
	private String password;	// Spring Security에서는 pw가 아닌 password로 저장해줘야 한다.
	private String name;	// 이건 우리가 필요해서 만든 것 (자유)
	
	
	// 인가 : 이 회원이 접근할 수 있는 범위
	@Enumerated(EnumType.STRING)	// 열거형이다
	private Role role;	// 계정의 권한을 저장하고 있을 변수(필드) - 일반, 매니저, 관리자
	
	private boolean enbled;	// 계정의 활성화 / 비활성화
	
	
}
