package com.example.demo.user.service;

import com.example.demo.user.dto.UserApiResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileUploadService {

    private static final String UPLOAD_DIR = System.getProperty("user.dir") + "/uploads/";

    public UserApiResponseDto uploadFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("파일이 없습니다.");
        }
        try {
            createUploadDirectoryIfNotExists();
            String fileName = generateUniqueFileName(file.getOriginalFilename());
            Path filePath = Paths.get(UPLOAD_DIR, fileName);
            file.transferTo(filePath.toFile());
            return new UserApiResponseDto("http://localhost:8080/uploads/" + fileName);
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 중 오류 발생", e);
        }
    }

    public UserApiResponseDto uploadFile(MultipartFile file, String userId) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("파일이 없습니다.");
        }
        try {
            createUploadDirectoryIfNotExists();

            // 기존 파일 덮어쓰기 (파일명을 사용자 ID로 지정)
            String fileName = userId + ".png";
            Path filePath = Paths.get(UPLOAD_DIR, fileName);
            file.transferTo(filePath.toFile());

            return new UserApiResponseDto("http://localhost:8080/uploads/" + fileName);
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 중 오류 발생", e);
        }
    }

    private void createUploadDirectoryIfNotExists() {
        File directory = new File(UPLOAD_DIR);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    private String generateUniqueFileName(String originalFilename) {
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        return UUID.randomUUID() + extension;
    }
}
