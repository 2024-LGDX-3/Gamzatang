package com.lgedx.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lgedx.entity.Member;
import com.lgedx.service.MemberService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller // Controller 역할을 하는 class임을 의미
public class MemberController {
	// 기존 controller는 class 형태로 제작
	// SpringBoot에서는 업무 단위별로 Controller를 생성
	// ex) BoardController, ProductController 등 ..
	// class 형태로 제작되던 controller는 메소드 형태로 진행
	@Autowired
	MemberService memberService;

	// 모든 요청은 Controller를 거쳐서 진행
	@GetMapping("/join")
	public String JoinPage() {
		return "Join"; // prefix + Join + suffix
	}
	
	@GetMapping("/main")
	public String Main() {
		return "Main";
	}
	
	@GetMapping("/login")
	public String loginPage() {
		return "Login";
	}

	@GetMapping("/loginfail")
	public String LoginFail() {
		return "LoginFail";
	}
	
	@PostMapping("/join")
	// 입력받은 id, pw, name갑은
	// Spring내부에서 자동으로 Member 엔티티로 인식하여 매개변수로 받아준다!
	public String join(Member member, Model model) {
		Member joinMember = memberService.join(member);
		if (joinMember != null) {
			return "Login";
		} else {
//			model.addAttribute("errorMessage", "회원가입에 실패하였습니다.");
			return "Join";
		}
	}

	@PostMapping("/login")
	public String login(Member member, HttpServletRequest request) {
		Member login_user = memberService.login(member);
		if (login_user != null) {
			// session 생성
			HttpSession session = request.getSession();
			session.setAttribute("login_user", login_user);

			return "Main";
		} else {
			return "LoginFail";
		}
	}

}
