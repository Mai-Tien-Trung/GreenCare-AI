package com.example.GreenCareAI.controller;

import com.example.GreenCareAI.service.PlantDiseaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/api/ai")
public class PlantDiseaseController {

    @Autowired
    private PlantDiseaseService service;

    @PostMapping("/detect")
    public ResponseEntity<String> detectPlantDisease(@RequestParam("file") MultipartFile file) throws IOException {
        File tempFile = File.createTempFile("leaf-", ".jpg");
        file.transferTo(tempFile);

        String result = service.detectDisease(tempFile.getAbsolutePath());
        tempFile.delete();

        return ResponseEntity.ok(result);
    }
}
