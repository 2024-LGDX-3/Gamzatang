package kr.spring.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import kr.spring.entity.CustomUser;
import kr.spring.entity.Member;
import kr.spring.repository.MemberRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{

	// Controller와 내가 만든 JPA 사이를 연결해주는 Spring Security 연결체
	
	@Autowired
	private MemberRepository memberRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// Spring Security 로그인 기능
		// 아이디랑 일치하는 회원이 있는지 검색 (내부적으로 알아서 진행함)
		// 로그인에 성공하면 username에는 성공한 사람의 id가 담겨있음
		Member member = memberRepository.findById(username).get();
		
		if(member == null) {
			// 로그인 실패
			throw new UsernameNotFoundException(username + "없음");
		}
		
		
		return new CustomUser(member);	// User (3가지 권한정보) + Member(회원정보) -> UserDetails 타입으로 변환 (업캐스팅)
	}

	

}
