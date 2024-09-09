package com.lgedx.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lgedx.entity.Member;
import com.lgedx.repository.MemberRepository;

@Service
public class MemberService {
	// 요청 : Controller - Service - Repository
	// 응답 : Repository - Service - Controller

	// Controller : 요청을 받고 응답하는 것에 집중
	// Service : 데이터를 가공하는 작업에 집중(비즈니스로직)
	// Repository : 쿼리를 자동생성해주는 메소드의 집합(JpaRepository 상속)
	@Autowired
	MemberRepository memberRepository;

	// 회원가입
	public Member join(Member member) {
		// memberRepository에 save메소드를 활용해서
		// DB에 입력받아온 Member(id, pw, name)형태로 insert 하겠다!
		return memberRepository.save(member);

	}

	public Member login(Member member) {
		return memberRepository.findByIdAndPw(member.getId(), member.getPw());
	}

	public String idCheck(String id) {
		Optional<Member> user = memberRepository.findById(id);
		
		if (user.isPresent()) {
			return "중복되는 아이디 입니다.";
		} else {
			return "사용 가능한 아이디 입니다.";
		}
		
	}

}
