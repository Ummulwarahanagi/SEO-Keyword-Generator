package com.example.keyword_generator.model;

import java.util.Map;

public class KeywordFrequencyResponse {

    private Map<String, Integer> keywordFrequency;

    public KeywordFrequencyResponse(Map<String, Integer> keywordFrequency) {
        this.keywordFrequency = keywordFrequency;
    }

    public Map<String, Integer> getKeywordFrequency() {
        return keywordFrequency;
    }

    public void setKeywordFrequency(Map<String, Integer> keywordFrequency) {
        this.keywordFrequency = keywordFrequency;
    }
}