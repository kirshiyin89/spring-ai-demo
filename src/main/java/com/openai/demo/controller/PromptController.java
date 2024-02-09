package com.openai.demo.controller;

import com.openai.demo.service.PromptService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

    @RestController
    @RequiredArgsConstructor
    public class PromptController {

        private final PromptService promptService;

        @PostMapping("/starter")
        public ResponseEntity<String> generateSentenceStarter(@RequestBody Map<String, String> params) {
            String language = params.get("language");
            String topic = params.get("topic");
            return ResponseEntity.ok(promptService.generateSentences(language, topic));
        }

        @PostMapping("/feedback")
        public ResponseEntity<String> provideFeedback(@RequestBody Map<String, String> params) {
            String text = params.get("text");
            return ResponseEntity.ok(promptService.provideFeedback(text));
        }
    }
