package com.example.smart_stylist.controller;

import com.example.smart_stylist.entity.Outfit;
import com.example.smart_stylist.service.OutfitService;
import com.example.smart_stylist.dto.OutfitRequest;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/outfits")
public class OutfitController {

    private final OutfitService outfitService;

    public OutfitController(OutfitService outfitService) {
        this.outfitService = outfitService;
    }

    @PostMapping
    public ResponseEntity<Outfit> createOutfit(@Valid @RequestBody OutfitRequest request) {
        Outfit created = outfitService.createOutfit(request);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Outfit>> getAllOutfits() {
        List<Outfit> outfits = outfitService.getAllOutfits();
        return ResponseEntity.ok(outfits);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Outfit> getOutfitById(@PathVariable Long id) {
        Outfit outfit = outfitService.getOutfitById(id);
        return ResponseEntity.ok(outfit);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Outfit>> getOutfitsByUser(@PathVariable Long userId) {
        List<Outfit> outfits = outfitService.getOutfitsByUserId(userId);
        return ResponseEntity.ok(outfits);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Outfit> updateOutfit(@PathVariable Long id, @Valid @RequestBody OutfitRequest request) {
        Outfit updated = outfitService.updateOutfit(id, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOutfit(@PathVariable Long id) {
        outfitService.deleteOutfit(id);
        return ResponseEntity.noContent().build();
    }
}
