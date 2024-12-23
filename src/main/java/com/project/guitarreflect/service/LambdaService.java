package com.project.guitarreflect.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.lambda.LambdaClient;
import software.amazon.awssdk.services.lambda.model.InvokeRequest;
import software.amazon.awssdk.services.lambda.model.InvokeResponse;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.core.exception.SdkException;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class LambdaService {

    private LambdaClient lambdaClient;
    private ObjectMapper objectMapper;

    public LambdaService() {
        // Lambda 클라이언트 생성
        this.lambdaClient = LambdaClient.create();
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Map으로 전달된 payload를 Lambda 함수에 전달
     * @param functionName 호출할 Lambda 함수 이름
     * @param payload Lambda에 전달할 Map 형식의 페이로드
     * @return Lambda 응답값
     */
    public Map<String, Object> invokeLambdaFunction(String functionName, Map<String, Object> payload) {
        try {
            // Map을 JSON 문자열로 변환
            String jsonPayload = objectMapper.writeValueAsString(payload);

            InvokeRequest invokeRequest = InvokeRequest.builder()
                    .functionName(functionName)
                    .payload(SdkBytes.fromUtf8String(jsonPayload))
                    .build();

            // Lambda 호출
            InvokeResponse invokeResponse = lambdaClient.invoke(invokeRequest);

            // 응답 페이로드를 String으로 변환
            String responsePayload = invokeResponse.payload().asUtf8String();

            // 응답을 Map<String, Object>로 변환하여 반환
            return objectMapper.readValue(responsePayload, Map.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        } catch (SdkException e) {
            e.printStackTrace();
            return null;
        }
    }


    public void close() {
        // Lambda 클라이언트 종료
        lambdaClient.close();
    }
}