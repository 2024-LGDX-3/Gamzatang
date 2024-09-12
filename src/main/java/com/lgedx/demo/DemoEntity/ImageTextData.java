package com.lgedx.demo.DemoEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

@Entity
@Table(name = "image_text_data")  // 테이블 이름을 지정
public class ImageTextData {

    @Id
    @Column(name = "image_url", length = 255, nullable = false)
    private String imageUrl;  // 기본 키로 사용할 이미지 URL

    @Column(name = "hash_tag", length = 100)
    private String hashTag;  // 해시태그를 저장하는 컬럼

    @Lob
    @Column(name = "text", nullable = false)  // "description" 대신 "text" 사용
    private String text;  // 설명(텍스트)을 저장하는 컬럼

    // 기본 생성자
    public ImageTextData() {}

    // 생성자
    public ImageTextData(String imageUrl, String hashTag, String text) {
        this.imageUrl = imageUrl;
        this.hashTag = hashTag;
        this.text = text;
    }

    // Getter 및 Setter 메서드
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getHashTag() {
        return hashTag;
    }

    public void setHashTag(String hashTag) {
        this.hashTag = hashTag;
    }

    public String getText() {  // "getDescription" 대신 "getText"
        return text;
    }

    public void setText(String text) {  // "setDescription" 대신 "setText"
        this.text = text;
    }
}