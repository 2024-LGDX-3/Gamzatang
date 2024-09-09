package com.lgedx.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ThinQController {
    @GetMapping("/ThinQ")
    public String showThinQPage() {
        return "ThinQ"; // 'ThinQ.html' 파일을 보여줍니다.
    }
}






