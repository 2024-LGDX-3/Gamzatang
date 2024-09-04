package kr.spring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/member/*") // localhost:8081/boot/member/*
public class MemberController {

	@GetMapping("/login")
	public String login() {
		return "member/login";
	}
	
	@RequestMapping("/success")
	public String success() {
		return "member/success";
	}
	
	
}
