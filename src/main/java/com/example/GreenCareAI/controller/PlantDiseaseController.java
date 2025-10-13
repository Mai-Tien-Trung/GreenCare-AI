package com.example.GreenCareAI.controller;

import com.example.GreenCareAI.service.PlantDiseaseService;
import com.example.GreenCareAI.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/api/ai")
public class PlantDiseaseController {

    @Autowired
    private PlantDiseaseService plantDiseaseService;

    @Autowired
    private SubscriptionService subscriptionService;

    @PostMapping("/detect")
    public ResponseEntity<String> detectPlantDisease(
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal UserDetails userDetails
    ) throws IOException {
        subscriptionService.deductScanByUsername(userDetails.getUsername());


        File tempFile = File.createTempFile("leaf-", ".jpg");
        file.transferTo(tempFile);

        String result = plantDiseaseService.detectDisease(tempFile.getAbsolutePath());
        tempFile.delete();

        return ResponseEntity.ok(result);
    }
}
