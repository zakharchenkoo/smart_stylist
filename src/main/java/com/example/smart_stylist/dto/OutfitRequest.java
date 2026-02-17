package com.example.smart_stylist.dto;

import jakarta.validation.constraints.*;
import java.util.List;
public class OutfitRequest {
    @NotBlank(message = "Outfit name cannot be blank")
    @Size(max = 150, message = "Outfit name max length is 150")
    private String name;

    @NotEmpty(message = "Outfit must have at least one clothes ID")
    private List<Long> clothesIds;

    @NotNull(message = "User ID cannot be null")
    private Long userId;

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Long> getClothesIds() {
        return clothesIds;
    }

    public void setClothesIds(List<Long> clothesIds) {
        this.clothesIds = clothesIds;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
