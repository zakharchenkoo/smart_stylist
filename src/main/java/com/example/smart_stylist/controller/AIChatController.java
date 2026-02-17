package com.example.smart_stylist.controller;

import com.example.smart_stylist.dto.ChatMessage;
import com.example.smart_stylist.dto.ChatRequest;
import com.example.smart_stylist.service.AIChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ai-chat")
@CrossOrigin(origins = "http://localhost:3000")
public class AIChatController {

    @Autowired
    private AIChatService aiChatService;

    @GetMapping("/start")
    public ResponseEntity<ChatMessage> startChat() {
        ChatMessage initialMessage = aiChatService.startConversation();
        return ResponseEntity.ok(initialMessage);
    }

    @PostMapping("/message")
    public ResponseEntity<?> sendMessage(@RequestBody ChatRequest request) {
        try {
            ChatMessage response = aiChatService.continueConversation(
                    request.getUserId(),
                    request.getMessages()
            );

            boolean isFinal = response.getContent().contains("RECOMMENDATION:");
            List<Long> clothesIds = List.of();

            if (isFinal) {
                clothesIds = aiChatService.extractClothesIds(response.getContent());
            }

            Map<String, Object> result = new HashMap<>();
            result.put("message", response);
            result.put("isFinal", isFinal);
            result.put("clothesIds", clothesIds);

            return ResponseEntity.ok(result);

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to process message: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }
}
