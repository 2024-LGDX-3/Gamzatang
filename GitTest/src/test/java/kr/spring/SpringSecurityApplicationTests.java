package kr.spring;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import kr.spring.entity.Member;
import kr.spring.entity.Role;
import kr.spring.repository.MemberRepository;

@SpringBootTest
class SpringSecurityApplicationTests {
	
	@Autowired
	private MemberRepository memberRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	

	@Test
	void contextLoads() {
		Member m = new Member();
		m.setUsername("jye");
		m.setPassword(passwordEncoder.encode("1234"));
		m.setName("지영은");
		m.setRole(Role.ADMIN);
		m.setEnbled(true);
		memberRepository.save(m);
	}

}
