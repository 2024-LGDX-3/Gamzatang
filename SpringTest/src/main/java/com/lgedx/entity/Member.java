package com.lgedx.entity;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity // 테이블로 만들어주느 어노테이션
@Data // 자동으로 getter 등 필요한 메소드들을 만들어주는 어노테이션
public class Member {

   @Id // PK 지정
   //@GeneratedValue(strategy = GenerationType.SEQUENCE) 자동증가컬럼
   private String id ;
   private String pw ;
   private String name ;
   private String age ;
   private String gender ;
   private String phone ;
   // name : 필드 이름과 다르게 컬럼이름 설정
   // columnDefinition : 컬럼 추가 속성 지정
   // insertable 값을 추가 할때 해당 컬럼은 제외
   // updatable 값을 수정할때 해당 컬럼은 제외
   @Column(insertable = false ,updatable = false, columnDefinition="DATE DEFAULT SYSDATE")
   private Date joinDate;

}
