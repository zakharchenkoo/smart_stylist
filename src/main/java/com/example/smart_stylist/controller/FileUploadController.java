package com.example.smart_stylist.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/upload")
@CrossOrigin(origins = "http://localhost:3000")
public class FileUploadController {

    private static final String UPLOAD_DIR = "uploads/";

    @PostMapping
    public ResponseEntity<Map<String, String>> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new RuntimeException("File is empty");
            }

            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                throw new RuntimeException("File must be an image");
            }

            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename != null && originalFilename.contains(".")
                    ? originalFilename.substring(originalFilename.lastIndexOf("."))
                    : ".jpg";
            String fileName = UUID.randomUUID().toString() + extension;
            Path filePath = Paths.get(UPLOAD_DIR + fileName);

            Files.write(filePath, file.getBytes());

            String fileUrl = "http://localhost:8080/uploads/" + fileName;

            Map<String, String> response = new HashMap<>();
            response.put("url", fileUrl);

            System.out.println("File uploaded successfully: " + fileUrl);

            return ResponseEntity.ok(response);

        } catch (IOException e) {
            System.err.println("Upload failed: " + e.getMessage());
            throw new RuntimeException("Failed to upload file: " + e.getMessage(), e);
        }
    }
}
