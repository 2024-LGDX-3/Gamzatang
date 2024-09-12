package com.lgedx.demo.Repository;

import com.lgedx.demo.DemoEntity.ImageTextData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageTextDataRepository extends JpaRepository<ImageTextData, String> {
    // 기본 CRUD 메서드는 JpaRepository가 제공
}