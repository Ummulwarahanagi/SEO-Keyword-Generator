package com.example.keyword_generator.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class KeywordResponse {

    private String keyword;
    private double score;

    // ✅ REQUIRED for Redis/Jackson
    public KeywordResponse() {}

    @JsonCreator
    public KeywordResponse(
            @JsonProperty("keyword") String keyword,
            @JsonProperty("score") double score
    ) {
        this.keyword = keyword;
        this.score = score;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}