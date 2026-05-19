package com.example.keyword_generator.controller;

import com.example.keyword_generator.model.KeywordRequest;
import com.example.keyword_generator.model.KeywordResponse;
import com.example.keyword_generator.service.KeywordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class KeywordController {

    @Autowired
    private KeywordService keywordService;

    @PostMapping("/generate-keywords")
    public ResponseEntity<List<KeywordResponse>> generateKeywords(
            @RequestBody KeywordRequest request) {

        return ResponseEntity.ok(
                keywordService.generateKeywords(request.getText())
        );
    }

    @PostMapping("/keyword-frequency")
    public ResponseEntity<Map<String, Integer>> frequency(
            @RequestBody KeywordRequest request) {

        return ResponseEntity.ok(
                keywordService.generateKeywordFrequency(request.getText())
        );
    }
} 
