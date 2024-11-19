package com.project.guitarreflect.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Map;

@Slf4j
@Service
public class CurrencyService {
    private final S3Client s3Client;
    private final ObjectMapper objectMapper;
    private final String localFilePath = "src/main/resources/json/exchange-rates.json";

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @Value("${aws.s3.key}")
    private String s3Key;

    public CurrencyService(@Value("${aws.s3.region}") String region,
                           @Value("${aws.s3.access-key}") String accessKey,
                           @Value("${aws.s3.secret-key}") String secretKey,
                           ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(accessKey, secretKey);
        this.s3Client = S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                .build();


    }

    public void downloadFileFromS3() {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(s3Key)
                .build();

        try (InputStream s3Stream = s3Client.getObject(getObjectRequest);
             FileOutputStream fos = new FileOutputStream(localFilePath)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = s3Stream.read(buffer)) > 0) {
                fos.write(buffer, 0, length);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Map<String, Object> getLocalData() {
        try {
            File file = Paths.get(localFilePath).toFile();
            return objectMapper.readValue(file, Map.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
