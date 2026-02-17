package com.example.smart_stylist.dto;

import java.util.List;

public class ChatRequest {
    private Long userId;
    private List<ChatMessage> messages;

    // Getters and setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<ChatMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<ChatMessage> messages) {
        this.messages = messages;
    }
}
