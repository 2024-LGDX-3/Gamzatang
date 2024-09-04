package kr.spring.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {
	// Spring Security 환경설정 파일
	
	@Autowired
	private UserDetailsServiceImpl userService;
	
	@Bean
	public PasswordEncoder passwordEncoder() {	// 비밀번호 암호화
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
										// HttpSecurity : Spring Security 모든 권한을 가지고 있는 객체
		// 1. CSRF 토큰 비활성화
		http.csrf().disable();
		
		// 2. 접근제어
		http.authorizeHttpRequests()	// 사용자의 요청을 무조건 제어하겠다.
			// /또는 member폴더 하위에 대해서 접근은 무조건 허용하겠다.
			.antMatchers("/", "/member/**").permitAll()
			// board 경로 접근은 인증된 사용자만 허용한다
			.antMatchers("/board/**").authenticated()
			.and()
			.formLogin()	// 별도의 로그인 폼을 사용하겠다.
			.loginPage("/member/login")
			.defaultSuccessUrl("/member/success") // 로그인 성공 시 이동할 페이지
			.and()
			.logout()	// 로그아웃 기능 사용하겠다
			.logoutUrl("/member/logout") // 해당 url 요청 시 로그아웃 기능 수행
			.logoutSuccessUrl("/member/login");
		
		http.userDetailsService(userService);
			
		return http.build();
	}
	
}
