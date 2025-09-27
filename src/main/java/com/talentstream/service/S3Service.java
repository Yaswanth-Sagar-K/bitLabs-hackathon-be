package com.talentstream.service;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
public class S3Service {

    private final S3Client s3Client;

    @Value("${application.bucket.name}")
    private String bucketName;

    public S3Service(
            @Value("${cloud.aws.credentials.access-key}") String accessKey,
            @Value("${cloud.aws.credentials.secret-key}") String secretKey,
            @Value("${cloud.aws.region.static}") String region) {

        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(accessKey, secretKey);

        this.s3Client = S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .build();
    }

    // Upload video → goes to "videos/" folder
    @Async
    public String uploadVideo(MultipartFile file) {
        try {
            return uploadFileToFolder(file, "Videos/");
        } catch (IOException e) {
            System.err.println("Failed to upload video: " + e.getMessage());
            return null;
        }
    }

    // Upload thumbnail → goes to "thumbnails/" folder
    @Async
    public String uploadThumbnail(MultipartFile file) {
        try {
            return uploadFileToFolder(file, "thumbnails/");
        } catch (IOException e) {
            System.err.println("Failed to upload thumbnail: " + e.getMessage());
            return null;
        }
    }
  
    private String uploadFileToFolder(MultipartFile file, String folder) throws IOException {
        try {
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }

            String uniqueFileName = folder + UUID.randomUUID().toString() + extension;

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(uniqueFileName)
                    .contentType(file.getContentType() != null ? file.getContentType() : "application/octet-stream")
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));

            String region = s3Client.serviceClientConfiguration().region().id();
            return "https://" + bucketName + ".s3." + region + ".amazonaws.com/" + uniqueFileName;
        } catch (Exception e) {
            System.err.println("Error uploading file to S3: " + e.getMessage());
            return null;
        }
    }

    @Async
    public void deleteFile(String fileUrl) {
        if (fileUrl == null || fileUrl.isEmpty()) {
            System.out.println("No file to delete");
            return;
        }

        try {
            // Extract key reliably
            String[] parts = fileUrl.split(".amazonaws.com/");
            if (parts.length < 2) {
                System.out.println("Invalid S3 URL: " + fileUrl);
                return;
            }
            String key = parts[1]; // everything after .amazonaws.com/

            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            s3Client.deleteObject(deleteObjectRequest);
            System.out.println("Deleted file from S3: " + key);
        } catch (Exception e) {
            System.err.println("Failed to delete file: " + e.getMessage());
        }
    }
}
