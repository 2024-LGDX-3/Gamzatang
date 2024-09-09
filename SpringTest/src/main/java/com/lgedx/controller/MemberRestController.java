package com.lgedx.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lgedx.service.MemberService;

@RestController
// @ResponseBody + @Controller = @RestController
// 데이터를 응답하는 Controller
public class MemberRestController {
	
	@Autowired
	MemberService memberService;

	@GetMapping("/idCheck")
	public String idCheck(String id) {
		System.out.println("id : " + id);
		String result = memberService.idCheck(id);
		
		return result;
	}

}
