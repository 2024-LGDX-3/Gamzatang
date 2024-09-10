package com.lgedx.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Controller
public class MyController {

    @GetMapping("/thinq")
    public String thinqPage() {
        return "thinq";
    }

    @GetMapping("/linq")
    public String linqPage() {
        return "linq";
    }

    @GetMapping("/diary")
    public String diaryPage() {
        return "diary";
    }

    @GetMapping("/standby")
    public String sbPage() {
        return "standby";
    }

   @GetMapping("/oshome")
   public String oshomePage() {return "osHome";}


    @GetMapping("/album") // 다른 경로로 수정됨
    public String album(Model model) {
        // 이미지가 저장된 폴더 경로 (diary 폴더)
        String folderPath = "diary";
        File folder = new File(folderPath);

        // 이미지 파일 경로 목록을 담을 리스트
        List<String> imagePaths = new ArrayList<>();

        // 폴더에서 파일 리스트 가져오기
        File[] listOfFiles = folder.listFiles();
        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                if (file.isFile() && (file.getName().endsWith(".jpg") || file.getName().endsWith(".png"))) {
                    imagePaths.add("diary/" + file.getName()); // 이미지 파일 경로 추가
                }
            }
        }

        // 모델에 이미지 경로 목록 추가
        model.addAttribute("images", imagePaths);
        return "album"; // album.html 템플릿 반환
    }
}
