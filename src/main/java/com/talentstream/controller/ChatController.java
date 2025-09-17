package com.talentstream.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.talentstream.dto.MessageRequest;
import com.talentstream.dto.MessageResponse;

import java.util.Map;

@RestController
public class ChatController {

    @Autowired
    private com.talentstream.service.GroqService groqService;

    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> root() {
        return ResponseEntity.ok(groqService.getStatus());
    }

    @GetMapping("/test-groq")
    public ResponseEntity<MessageResponse> testGroq() {
        MessageResponse response = groqService.testGroqConnection();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/chat")
    public ResponseEntity<MessageResponse> chat(@Valid @RequestBody MessageRequest messageRequest) {
        MessageResponse response = groqService.sendMessage(messageRequest.getMessage());
        return ResponseEntity.ok(response);
    }
}
