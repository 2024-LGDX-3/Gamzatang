package com.lgedx.demo.controller;

import com.lgedx.demo.DemoEntity.ImageTextData;
import com.lgedx.demo.Repository.ImageTextDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    @GetMapping("/diary") // 다른 경로로 수정됨
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
        return "diary";
    }

    @Autowired
    private ImageTextDataRepository imageTextDataRepository; // 리포지토리 주입

    @PostMapping("/getSelectedImagesData")
    @ResponseBody
    public List<ImageTextData> getSelectedImagesData(@RequestBody List<String> selectedImageUrls) {
        String baseUrl = "http://localhost:1524/demo/";

        // 절대 경로를 상대 경로로 변환
        List<String> relativeImageUrls = selectedImageUrls.stream()
                .map(url -> url.replace(baseUrl, "")) // 절대 경로를 제거하여 상대 경로로 변환
                .collect(Collectors.toList());

        // 선택한 이미지 URL을 로그로 확인
        System.out.println("Selected Images Relative URLs: " + relativeImageUrls);

        // DB에서 선택한 이미지 URL과 일치하는 데이터 가져오기
        List<ImageTextData> imageTextDataList = imageTextDataRepository.findByImageUrlIn(relativeImageUrls);

        // 가져온 데이터 확인
        System.out.println("DB에서 가져온 데이터: " + imageTextDataList);

        return imageTextDataList; // JSON 형식으로 반환
    }
    @GetMapping("/standby")
    public String sbPage(Model model) {
        try {
            // DB에서 image_url, hash_tag, text를 가져오기
            List<ImageTextData> imageTextDataList = imageTextDataRepository.findAll();

            // 이미지 경로 목록
            List<String> imagePaths = imageTextDataList.stream()
                    .map(ImageTextData::getImageUrl)
                    .collect(Collectors.toList());

            // 모델에 이미지 경로 및 기타 데이터 추가
            model.addAttribute("images", imageTextDataList);
            model.addAttribute("imagePaths", imagePaths);

            return "standby"; // standby.html 템플릿 반환
        } catch (Exception e) {
            e.printStackTrace(); // 오류 로그 출력
            model.addAttribute("error", "데이터를 가져오는 중 오류가 발생했습니다.");
            return "error"; // 오류 페이지로 이동
        }
    }


    @GetMapping("/oshome")
    public String oshomePage() {return "osHome";}



}
