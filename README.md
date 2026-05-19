# SEO Keyword Generation System

## Project Overview

The SEO Keyword Generation System is a Spring Boot based application that automatically generates SEO-friendly keywords from input content using keyword extraction and N-gram analysis techniques.

The system focuses on:

- Fast keyword generation
- SEO-oriented keyword phrases
- Low-latency execution
- Redis-based caching
- Ranked keyword scoring

---

# Objective

Build a low-latency keyword generation pipeline that:

- Extracts keywords from content
- Generates meaningful keyword phrases
- Optimizes keywords for SEO relevance
- Uses caching for performance improvement

---

# Technologies Used

## Backend
- Java 21
- Spring Boot
- Maven

## Database
- MySQL

## Cache
- Redis

## Frontend
- HTML
- CSS
- JavaScript

---

# Features Implemented

## 1. Keyword Extraction
The system extracts meaningful words from content after:

- Removing stop words
- Cleaning special characters
- Filtering small/non-relevant words

Example:

Input:
```text
Learn React for beginners step by step tutorial
