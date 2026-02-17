package com.example.smart_stylist.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public class EventRequest {
    @NotBlank(message = "Event name cannot be blank")
    @Size(max = 150, message = "Event name max length is 150")
    private String name;

    @NotBlank(message = "Event type cannot be blank")
    private String eventType;

    @NotNull(message = "Event date cannot be null")
    private LocalDate eventDate;

    private String description;

    @NotNull(message = "User ID cannot be null")
    private Long userId;

    private Long outfitId;

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public LocalDate getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDate eventDate) {
        this.eventDate = eventDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getOutfitId() {
        return outfitId;
    }

    public void setOutfitId(Long outfitId) {
        this.outfitId = outfitId;
    }
}
