package com.example.smart_stylist.controller;

import com.example.smart_stylist.dto.ClothesRequest;
import com.example.smart_stylist.entity.Clothes;
import com.example.smart_stylist.service.ClothesService;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clothes")
public class ClothesController {

    private final ClothesService clothesService;

    public ClothesController(ClothesService clothesService) {
        this.clothesService = clothesService;
    }

    @PostMapping
    public ResponseEntity<Clothes> addClothes(@Valid @RequestBody ClothesRequest request) {
        Clothes created = clothesService.addClothes(request);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Clothes>> getAllClothes() {
        List<Clothes> clothesList = clothesService.getAllClothes();
        return ResponseEntity.ok(clothesList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Clothes> getClothesById(@PathVariable Long id) {
        Clothes clothes = clothesService.getClothesById(id);
        return ResponseEntity.ok(clothes);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Clothes>> getClothesByUser(@PathVariable Long userId) {
        List<Clothes> clothes = clothesService.getClothesByUserId(userId);
        return ResponseEntity.ok(clothes);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Clothes> updateClothes(
            @PathVariable Long id,
            @Valid @RequestBody ClothesRequest request) {
        Clothes updated = clothesService.updateClothes(id, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClothes(@PathVariable Long id) {
        try {
            clothesService.deleteClothes(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to delete clothes: " + e.getMessage());
        }
    }
}
