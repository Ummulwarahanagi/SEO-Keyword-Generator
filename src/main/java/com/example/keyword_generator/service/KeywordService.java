package com.example.keyword_generator.service;

import com.example.keyword_generator.entity.KeywordEntity;
import com.example.keyword_generator.model.KeywordResponse;
import com.example.keyword_generator.repository.KeywordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class KeywordService {

    @Autowired
    private KeywordRepository keywordRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final Set<String> STOP_WORDS = Set.of(
            "a","an","the","is","are","was","were",
            "in","on","at","to","for","with","of",
            "and","or","but","this","that"
    );

    // =========================
    // MAIN PIPELINE
    // =========================
    public List<KeywordResponse> generateKeywords(String text) {

        if (text == null || text.isBlank()) {
            return new ArrayList<>();
        }

        String key = "keywords:" + text.toLowerCase();

        // CACHE SAFE READ
        Object cached = redisTemplate.opsForValue().get(key);
        if (cached instanceof List<?> cachedList) {
            return (List<KeywordResponse>) cachedList;
        }

        // STEP 1: N-GRAM GENERATION
        List<String> ngrams = generateNGrams(text);

        // STEP 2: CLEAN FILTERING
        List<String> cleanPhrases = ngrams.stream()
                .filter(this::isValidSeoPhrase)
                .distinct()
                .toList();

        // STEP 3: FREQUENCY MAP
        Map<String, Integer> freqMap = new HashMap<>();
        for (String phrase : cleanPhrases) {
            freqMap.put(phrase, freqMap.getOrDefault(phrase, 0) + 1);
        }

        // STEP 4: SCORING
        List<KeywordResponse> result = freqMap.entrySet()
                .stream()
                .map(e -> new KeywordResponse(
                        e.getKey(),
                        calculateScore(e.getKey(), freqMap)
                ))
                .sorted((a, b) -> Double.compare(b.getScore(), a.getScore()))
                .collect(Collectors.toList());

        // STEP 5: CACHE STORE
        redisTemplate.opsForValue().set(key, result);

        // STEP 6: DB SAVE
        KeywordEntity entity = new KeywordEntity();
        entity.setInputText(text);
        entity.setGeneratedKeywords(
                result.stream()
                        .map(KeywordResponse::getKeyword)
                        .collect(Collectors.joining(", "))
        );

        keywordRepository.save(entity);

        return result;
    }

    // =========================
    // N-GRAM GENERATION (CLEAN)
    // =========================
    private List<String> generateNGrams(String text) {

        String cleaned = text.toLowerCase()
                .replaceAll("[^a-zA-Z0-9 ]", " ");

        String[] words = Arrays.stream(cleaned.split("\\s+"))
                .map(String::trim)
                .filter(w -> !w.isEmpty())
                .filter(w -> !STOP_WORDS.contains(w))
                .filter(w -> w.length() > 2)
                .toArray(String[]::new);

        List<String> phrases = new ArrayList<>();

        // 1-GRAMS
        phrases.addAll(Arrays.asList(words));

        // 2-GRAMS (NO REPEATED WORDS)
        for (int i = 0; i < words.length - 1; i++) {
            String bigram = words[i] + " " + words[i + 1];
            if (!hasDuplicateWords(bigram)) {
                phrases.add(bigram);
            }
        }

        // 3-GRAMS (NO REPEATED WORDS)
        for (int i = 0; i < words.length - 2; i++) {
            String trigram = words[i] + " " + words[i + 1] + " " + words[i + 2];
            if (!hasDuplicateWords(trigram)) {
                phrases.add(trigram);
            }
        }

        return phrases;
    }

    // =========================
    // REMOVE BAD PHRASES LIKE "step step"
    // =========================
    private boolean hasDuplicateWords(String phrase) {
        String[] words = phrase.split(" ");
        Set<String> set = new HashSet<>(Arrays.asList(words));
        return set.size() != words.length;
    }

    // =========================
    // SEO VALIDATION
    // =========================
    private boolean isValidSeoPhrase(String phrase) {

        if (phrase == null || phrase.isBlank()) return false;

        String[] words = phrase.split(" ");

        if (words.length == 1 && phrase.length() < 3) return false;

        // reject repetitive phrases
        if (hasDuplicateWords(phrase)) return false;

        // avoid too short garbage
        return phrase.length() >= 3;
    }

    // =========================
    // SCORING ENGINE
    // =========================
    private double calculateScore(String phrase, Map<String, Integer> freqMap) {

        int freq = freqMap.getOrDefault(phrase, 1);

        double freqScore = freq * 2.0;
        double lengthScore = phrase.split(" ").length >= 2 ? 3.0 : 1.0;
        double seoBoost = phrase.length() > 10 ? 2.0 : 1.0;

        return freqScore + lengthScore + seoBoost;
    }

    // =========================
    // FREQUENCY (OPTIONAL API)
    // =========================
    public Map<String, Integer> generateKeywordFrequency(String text) {

        if (text == null || text.isBlank()) {
            return new HashMap<>();
        }

        String cleaned = text.toLowerCase().replaceAll("[^a-zA-Z0-9 ]", " ");
        String[] words = cleaned.split("\\s+");

        Map<String, Integer> map = new HashMap<>();

        for (String w : words) {
            if (!STOP_WORDS.contains(w) && w.length() > 2) {
                map.put(w, map.getOrDefault(w, 0) + 1);
            }
        }

        return map;
    }
}