package com.example.demo.user.controller;

import com.example.demo.user.dto.UserApiResponseDto;
import com.example.demo.user.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class FileUploadController {

    private final FileUploadService fileUploadService;


    @PostMapping("/users/images-upload")
    public ResponseEntity<UserApiResponseDto> uploadFile(@RequestParam("file") MultipartFile file) {
        UserApiResponseDto response = fileUploadService.uploadFile(file);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/users/{userId}/user-profile/uploads")
    public ResponseEntity<UserApiResponseDto> updateProfileImage(@PathVariable String userId, @RequestParam("file") MultipartFile file) {
        UserApiResponseDto response = fileUploadService.uploadFile(file, userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/uploads/{filename:.+}")
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        try {
            Path filePath = Paths.get(System.getProperty("user.dir") + "/uploads/").resolve(filename);
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                return ResponseEntity.notFound().build();
            }

            String contentType = Files.probeContentType(filePath);
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}