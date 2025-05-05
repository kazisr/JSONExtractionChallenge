package com.json.extraction.challenge.kazi.controller;

import com.json.extraction.challenge.kazi.model.ImageRequest;
import com.json.extraction.challenge.kazi.service.JsonExtractorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ImageController {
    @Autowired
    private JsonExtractorService extractorService;

    @PostMapping("/extract-json")
    public ResponseEntity<Map<String, Object>> extractJson(@RequestBody ImageRequest request) {
        Map<String, Object> response = new HashMap<>();
        try {
            Map<String, Object> data = extractorService.extractJsonFromImage(request.getImageBase64());
            response.put("success", true);
            response.put("data", data);
            response.put("message", "Successfully extracted JSON from image");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("data", null);
            response.put("message", "Failed to extract JSON: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
