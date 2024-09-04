package kr.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.spring.entity.Member;

public interface MemberRepository extends JpaRepository<Member, String>{
	// DataBase와 연동을 통해 값을 가져오거나 넣어주는 인터페이스
	

}
