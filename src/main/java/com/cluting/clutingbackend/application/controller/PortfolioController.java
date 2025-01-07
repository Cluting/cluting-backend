package com.cluting.clutingbackend.application.controller;

import com.cluting.clutingbackend.application.service.PortfolioService;
import com.cluting.clutingbackend.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/applicant")
@Tag(name = "지원자 포트폴리오 API")
public class PortfolioController {

    private final PortfolioService portfolioService;

    @PostMapping("/upload/file")
    @Operation(summary = "지원자 포트폴리오 pdf 업로드")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file, @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (file.isEmpty() || !file.getContentType().equals("application/pdf")) {
            return ResponseEntity.badRequest().body("Only PDF files are allowed.");
        }

        try {
            String fileUrl = portfolioService.uploadFile(file,userDetails);
            return ResponseEntity.ok("File uploaded successfully: " + fileUrl);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while uploading file: " + e.getMessage());
        }
    }

    @PostMapping("/upload/link")
    @Operation(summary = "지원자 포트폴리오 링크 업로드")
    public ResponseEntity<String> uploadLink(@RequestParam("link") String link, @AuthenticationPrincipal CustomUserDetails userDetails) throws IOException {
        String res = portfolioService.uploadLink(link,userDetails);
      return ResponseEntity.ok(res);
    }
}

