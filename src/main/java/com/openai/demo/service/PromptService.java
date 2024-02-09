package com.openai.demo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.client.AiClient;
import org.springframework.ai.prompt.AssistantPromptTemplate;
import org.springframework.ai.prompt.Prompt;
import org.springframework.ai.prompt.PromptTemplate;
import org.springframework.ai.prompt.SystemPromptTemplate;
import org.springframework.ai.prompt.messages.Message;
import org.springframework.ai.prompt.messages.UserMessage;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PromptService {

    private final AiClient aiClient;

    public String generateSentences(String language, String topic) {
        String userText = """
                Start a sentence in {language} about this topic {topic} and ask the student to think about a
                 continuaton of the story to practice grammar and new words. If the sentence is in Japanese,
                 always write back in Hiragana and provide the Romaji equivalent in brackets. Also, translate it to English.
                 """;
        PromptTemplate userPromptTemplate = new PromptTemplate(userText);
        Message userMessage = userPromptTemplate.createMessage(Map.of("language", language, "topic", topic));

        String systemText = """
                You are a helpful AI assistant that helps students in practicing foreign languages. Respond in the style of an encouraging teacher
                """;
        SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(systemText);

        Message systemMessage = systemPromptTemplate.createMessage();

        Prompt prompt = new Prompt(List.of(userMessage, systemMessage));
        return aiClient.generate(prompt).getGeneration().getText();
    }

    public String provideFeedback(String userText) {
        Message userMessage = new UserMessage("Is this sentence correct: " + userText);

        String instructions = """
                You are a helpful AI assistant that helps students in practicing foreign languages.
                You should provide feedback to the students and correct the grammar and make the sentence in the foreign language sound native.
                Check and correct the user text {text}. Tell the student if the sentence is correct. If the sentence is in Japanese,
                always write back in Hiragana and provide the Romaji equivalent in brackets.
                """;
        AssistantPromptTemplate assistantPromptTemplate = new AssistantPromptTemplate(instructions);
        Message assistantPromptTemplateMessage = assistantPromptTemplate.createMessage(Map.of("text", userText));

        Prompt prompt = new Prompt(List.of(userMessage, assistantPromptTemplateMessage));
        return aiClient.generate(prompt).getGeneration().getText();
    }
}
