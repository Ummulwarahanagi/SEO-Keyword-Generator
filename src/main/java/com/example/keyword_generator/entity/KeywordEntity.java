package com.example.keyword_generator.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "keywords")
public class KeywordEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String inputText;

    @Column(columnDefinition = "TEXT")
    private String generatedKeywords;

    public Long getId() {
        return id;
    }

    public String getInputText() {
        return inputText;
    }

    public void setInputText(String inputText) {
        this.inputText = inputText;
    }

    public String getGeneratedKeywords() {
        return generatedKeywords;
    }

    public void setGeneratedKeywords(String generatedKeywords) {
        this.generatedKeywords = generatedKeywords;
    }
}