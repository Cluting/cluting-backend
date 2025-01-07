package com.cluting.clutingbackend.application.service;

import com.cluting.clutingbackend.global.security.CustomUserDetails;
import com.cluting.clutingbackend.user.domain.User;
import com.cluting.clutingbackend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class PortfolioService {

    private final S3Client s3Client;
    private final UserRepository userRepository;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucketName;

    public String uploadFile(MultipartFile file, CustomUserDetails userDetails) throws IOException {

        String bucketName = this.bucketName;
        String folderName = "portfolio"; // Optional: S3에 파일을 저장할 폴더 이름
        String fileName = folderName + "/" + file.getOriginalFilename();

        try {
            s3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(fileName)
                            .contentType(file.getContentType())
                            .build(),
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize())
            );

            String pdfLink = "https://" + bucketName + ".s3.amazonaws.com/" + fileName;

            User user = userRepository.findById(userDetails.getId())
                    .orElseThrow(()->new RuntimeException("User Not Found"));

            user.setPortfolioFile(pdfLink);
            userRepository.save(user);

            return pdfLink;
        } catch (S3Exception e) {
            throw new RuntimeException("Failed to upload file to S3", e);
        }
    }

    public String uploadLink(String link, CustomUserDetails userDetails) throws IOException {
        User user = userRepository.findById(userDetails.getId())
                .orElseThrow(()->new RuntimeException("User Not Found"));

        user.setPortfolioUrl(link);
        userRepository.save(user);

        return link + "is Uploaded!";
    }
}
