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

import java.io.*;
import java.nio.file.Paths;
import java.util.Map;

@Slf4j
@Service
public class CurrencyService {
    private S3Client s3Client;
    private ObjectMapper objectMapper;
    private final String filePath = "json/exchange-rates.json";

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @Value("${aws.s3.key}")
    private String s3Key;

    public CurrencyService(@Value("${aws.s3.region}") String region,
                           @Value("${aws.s3.access-key}") String accessKey,
                           @Value("${aws.s3.secret-key}") String secretKey,
                           ObjectMapper objectMapper) {
        log.info(region);
        log.info(accessKey);
        log.info(secretKey);
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
             FileOutputStream fos = new FileOutputStream(filePath)) {
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
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePath)) {
            if (inputStream == null) {
                throw new FileNotFoundException("리소스를 찾을 수 없습니다");
            }
            return objectMapper.readValue(inputStream, Map.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
