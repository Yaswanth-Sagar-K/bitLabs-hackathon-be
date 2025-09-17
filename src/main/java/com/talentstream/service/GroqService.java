package com.talentstream.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.talentstream.dto.GroqMessage;
import com.talentstream.dto.GroqRequest;
import com.talentstream.dto.GroqResponse;
import com.talentstream.dto.MessageResponse;


import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class GroqService {
    
    private final WebClient webClient;
    private final List<String> skills = Arrays.asList("CSS", "React", "Java", "C", "JavaScript", "HTML");
    private final List<GroqMessage> conversationMemory = new ArrayList<>();
    
    @Value("${GROQ_API_KEY}")
    private String groqApiKey;
    
    @Value("${groq.api.url:https://api.groq.com/openai/v1/chat/completions}")
    private String groqApiUrl;

    public GroqService() {
        this.webClient = WebClient.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(1024 * 1024))
                .build();
        
        // Initialize conversation with system prompt
        initializeConversation();
        
        System.out.println("Extracted Skills (example): " + skills);
    }

    private void initializeConversation() {
        String skillsText = String.join(", ", skills);
        String systemPrompt = "The applicant has the following skills: " + skillsText + ".\n"
            + "You are an interview preparation assistant.\n"
            + "When generating interview questions:\n"
            + "- Always return them in a numbered list (1, 2, 3, 4, 5).\n"
            + "- Each question should be on a new line.\n"
            + "- After each question, provide a short explanation of what it tests.\n";
        
        conversationMemory.add(new GroqMessage("system", systemPrompt));
    }

    public MessageResponse testGroqConnection() {
        try {
            GroqRequest testRequest = new GroqRequest(
                "llama-3.1-8b-instant",
                List.of(new GroqMessage("user", "Hello, just testing the connection.")),
                50,
                0.7
            );

            GroqResponse response = webClient.post()
                    .uri(groqApiUrl)
                    .header("Authorization", "Bearer " + groqApiKey)
                    .header("Content-Type", "application/json")
                    .bodyValue(testRequest)
                    .retrieve()
                    .bodyToMono(GroqResponse.class)
                    .timeout(Duration.ofSeconds(10))
                    .block();

            return new MessageResponse(String.format(
                "Connection successful! Status: OK, API Key Length: %d, API Key Prefix: %s",
                groqApiKey.length(),
                groqApiKey.length() > 10 ? groqApiKey.substring(0, 10) + "..." : "Invalid"
            ));

        } catch (WebClientResponseException e) {
            return new MessageResponse(String.format(
                "API Error: %d - %s", e.getStatusCode().value(), e.getResponseBodyAsString()
            ));
        } catch (Exception e) {
            return new MessageResponse("Error: " + e.getMessage());
        }
    }

    public MessageResponse sendMessage(String userMessage) {
        try {
            // Add user message to conversation memory
            conversationMemory.add(new GroqMessage("user", userMessage));

            GroqRequest request = new GroqRequest(
                "llama-3.1-8b-instant",
                new ArrayList<>(conversationMemory),
                1000,
                0.7
            );

            GroqResponse response = webClient.post()
                    .uri(groqApiUrl)
                    .header("Authorization", "Bearer " + groqApiKey)
                    .header("Content-Type", "application/json")
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(GroqResponse.class)
                    .timeout(Duration.ofSeconds(30))
                    .block();

            if (response != null && response.getChoices() != null && !response.getChoices().isEmpty()) {
                String reply = response.getChoices().get(0).getMessage().getContent();
                
                // Add assistant response to conversation memory
                conversationMemory.add(new GroqMessage("assistant", reply));
                
                return new MessageResponse(reply);
            } else if (response != null && response.getError() != null) {
                return new MessageResponse("⚠️ API Response Error: " + response.getError().getMessage());
            } else {
                return new MessageResponse("⚠️ API Response Error: Unknown error");
            }

        } catch (WebClientResponseException e) {
            System.err.println("Groq API Error - Status Code: " + e.getStatusCode());
            System.err.println("Response: " + e.getResponseBodyAsString());
            return new MessageResponse("⚠️ API Error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
            
        } catch (Exception e) {
            if (e.getMessage().contains("timeout")) {
                return new MessageResponse("⚠️ Request timed out. Please try again.");
            } else if (e.getMessage().contains("Connection")) {
                return new MessageResponse("⚠️ Connection error. Please check your internet connection.");
            } else {
                System.err.println("Unexpected error: " + e.getMessage());
                return new MessageResponse("⚠️ Unexpected error: " + e.getMessage());
            }
        }
    }

    public Map<String, Object> getStatus() {
        return Map.of(
            "status", "Chatbot API running 🚀",
            "skills", skills,
            "conversationLength", conversationMemory.size()
        );
    }
}
