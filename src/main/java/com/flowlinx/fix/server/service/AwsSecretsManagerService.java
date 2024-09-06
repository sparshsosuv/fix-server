package com.flowlinx.fix.server.service;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AwsSecretsManagerService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public Map<String, String> getSecret() {
        String secretName = "prod/atlas/postgres/quickfix";
        Region region = Region.of("ap-south-1");

        // Create a Secrets Manager client
        SecretsManagerClient client = SecretsManagerClient.builder()
                .region(region)
                .build();

        GetSecretValueRequest getSecretValueRequest = GetSecretValueRequest.builder()
                .secretId(secretName)
                .build();

        GetSecretValueResponse getSecretValueResponse;

        try {
            getSecretValueResponse = client.getSecretValue(getSecretValueRequest);
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve secret from AWS Secrets Manager", e);
        }

        String secretString = getSecretValueResponse.secretString();

        try {
            return objectMapper.readValue(secretString, Map.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse secret JSON", e);
        }
    }

    public Map<String, String> getMongoSecret() {
        String secretName = "prod/atlas/mongo/sosuv-workflow";
        Region region = Region.of("ap-south-1");

        // Create a Secrets Manager client
        SecretsManagerClient client = SecretsManagerClient.builder()
                .region(region)
                .build();

        GetSecretValueRequest getSecretValueRequest = GetSecretValueRequest.builder()
                .secretId(secretName)
                .build();

        GetSecretValueResponse getSecretValueResponse;

        try {
            getSecretValueResponse = client.getSecretValue(getSecretValueRequest);
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve secret from AWS Secrets Manager", e);
        }

        String secretString = getSecretValueResponse.secretString();

        try {
            return objectMapper.readValue(secretString, Map.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse secret JSON", e);
        }
    }
}
