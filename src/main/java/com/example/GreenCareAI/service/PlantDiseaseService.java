package com.example.GreenCareAI.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Service
public class PlantDiseaseService {

    private final String hfToken = "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";
    private final String hfApiUrl =
            "https://router.huggingface.co/hf-inference/models/linkanjarad/mobilenet_v2_1.0_224-plant-disease-identification";

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String detectDisease(String imagePath) throws IOException {
        byte[] imageBytes = Files.readAllBytes(Path.of(imagePath));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        headers.setBearerAuth(hfToken);

        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        HttpEntity<byte[]> entity = new HttpEntity<>(imageBytes, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                hfApiUrl,
                HttpMethod.POST,
                entity,
                String.class
        );

        System.out.println("✅ Response status: " + response.getStatusCode());
        System.out.println("✅ Response body: " + response.getBody());

        // Parse JSON kết quả
        JsonNode root = objectMapper.readTree(response.getBody());
        if (root.isArray() && root.size() > 0) {
            JsonNode best = root.get(0);
            for (JsonNode node : root) {
                if (node.get("score").asDouble() > best.get("score").asDouble()) {
                    best = node;
                }
            }
            String label = best.get("label").asText();
            double score = best.get("score").asDouble() * 100;
            return String.format("Bệnh phát hiện: %s (%.2f%%)", label, score);
        }

        return "Không phát hiện được bệnh nào!";
    }
}
