package kr.spring.entity;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

import lombok.Data;

// 실제로 SpringSecurity에서는 회원의 정보를 저장하기 위해서는
// 자신이 만든 VO형태의 회원의 정보를 저장할 수가 없다.
// 자신이 만든 회원의 정보(VO)를 Security를 통해 저장하기 위해서
// 반드시 User 추상 클래스를 상속받은 클래스를 통해 저장해야 한다.
// User 추상 클래스는 SpringSecurity에서 회원 정보를 저장하기 위한
// UserDetails Interface를 구현해 놓은 클래스 이다.
@Data
public class CustomUser extends User{
	
	// 실제 저장될 회원의 정보 객체
	private Member member;

	public CustomUser(Member member) {
		super(member.getUsername(), member.getPassword(), AuthorityUtils.createAuthorityList("ROLE_" + member.getRole().toString()));
		this.member = member;
	}

}
