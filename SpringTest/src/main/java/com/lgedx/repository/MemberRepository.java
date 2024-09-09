package com.lgedx.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lgedx.entity.Member;

public interface MemberRepository extends JpaRepository<Member, String>{
	// JpaRepository : 필요한 쿼리를 자동으로 생성해주는 메소드들이 생성되어 있음
	// 만들어져 있는 메소드 외에 필요한 쿼리는 생성 -> 쿼리 메소드
	// https://docs.spring.io/spring-data/jpa/reference/jpa/query-methods.html
	// select * from member member where id = ? and pw = ?
	// find+By+컬럼이름1+And+컬럼이름2
	Member findByIdAndPw(String id, String pw);
	
}
