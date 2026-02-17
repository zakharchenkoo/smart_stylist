package com.example.smart_stylist.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
public class Clothes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
