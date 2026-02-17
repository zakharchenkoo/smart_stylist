package com.example.smart_stylist.dto;

import jakarta.validation.constraints.*;

public class ClothesRequest {
    @NotBlank(message = "Name cannot be blank")
    @Size(max = 100, message = "Name max length is 100")
    private String name;

    @NotBlank(message = "Category cannot be blank")
    @Pattern(
            regexp = "COATS_JACKETS|SWEATERS|BLAZERS_VESTS|SUITS|" +
                    "DRESSES_JUMPSUITS|SKIRTS|" +
                    "SHIRTS_BLOUSES|TSHIRTS_TOPS|" +
                    "JEANS_TROUSERS|SHORTS|" +
                    "SHOES|" +
                    "BAGS_ACCESSORIES|HATS_SCARVES|" +
                    "UNDERWEAR_BEACHWEAR|PAJAMAS",
            message = "Category must be one of the predefined categories"
    )
    private String category;

    @NotBlank(message = "Color cannot be blank")
    @Size(max = 30)
    private String color;

    @NotBlank(message = "Image URL cannot be blank")
    private String imageUrl;

    @NotNull(message = "User ID cannot be null")
    private Long userId;

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
