package com.lgedx.demo.controller;

import com.lgedx.demo.DemoEntity.ImageTextData;
import com.lgedx.demo.Repository.ImageTextDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class MyController {

    @Autowired
    private ImageTextDataRepository repository;

    @GetMapping("/thinq")
    public String thinqPage() {
        return "thinq";
    }

    @GetMapping("/linq")
    public String linqPage() {
        return "linq";
    }

    @GetMapping("/diary")
    public String diaryPage(Model model) {
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
        return "diary";  // diary.html 템플릿 반환
    }

    @GetMapping("/standby")
    public String sbPage() {
        return "standby";  // standby.html로 이동
    }

    @GetMapping("/oshome")
    public String oshomePage() {
        return "osHome";  // osHome.html로 이동
    }

    // 선택된 이미지와 더미 텍스트를 Oracle DB에 저장
    @PostMapping("/saveImageTextBatch")
    @ResponseBody
    public String saveImageTextBatch(@RequestBody List<Map<String, String>> items) {
        for (Map<String, String> item : items) {
            System.out.println("Received image URL: " + item.get("imageUrl"));
            System.out.println("Received hashTag: " + item.get("hashTag"));
            System.out.println("Received text: " + item.get("text"));

            String imageUrl = item.get("imageUrl");
            String hashTag = item.get("hashTag");
            String text = item.get("text");  // 더미 텍스트
            ImageTextData data = new ImageTextData(imageUrl, hashTag, text);
            repository.save(data);
        }
        return "{\"status\":\"success\"}";
    }
}