package com.lgedx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication // 해당 어노테이션을 통해서 Springboot 구동
// 해당 클래스는 하위의 설정 내용들을 읽기 위해서 프로젝트 최상단에 위치해야함
// com.lgedx
//	SpringBootApplication
// .controller
// .service
public class SpringTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringTestApplication.class, args);
	}

}
