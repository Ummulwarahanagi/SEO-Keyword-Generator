package com.example.keyword_generator.repository;

import com.example.keyword_generator.entity.KeywordEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KeywordRepository extends JpaRepository<KeywordEntity, Long> {
}