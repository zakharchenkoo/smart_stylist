package com.example.smart_stylist.service;

import com.example.smart_stylist.dto.ClothesRequest;
import com.example.smart_stylist.entity.Clothes;
import com.example.smart_stylist.entity.User;
import com.example.smart_stylist.exception.ResourceNotFoundException;
import com.example.smart_stylist.repository.ClothesRepository;
import com.example.smart_stylist.repository.UserRepository;
import org.springframework.stereotype.Service;
import com.example.smart_stylist.entity.Outfit;
import com.example.smart_stylist.repository.OutfitRepository;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Service
public class ClothesService {

    private final ClothesRepository clothesRepository;
    private final UserRepository userRepository;
    private final OutfitRepository outfitRepository;

    public ClothesService(ClothesRepository clothesRepository,
                          UserRepository userRepository,
                          OutfitRepository outfitRepository) {
        this.clothesRepository = clothesRepository;
        this.userRepository = userRepository;
        this.outfitRepository = outfitRepository;
    }

    public Clothes addClothes(ClothesRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + request.getUserId()));

        Clothes clothes = new Clothes();
        clothes.setName(request.getName());
        clothes.setCategory(request.getCategory());
        clothes.setColor(request.getColor());
        clothes.setImageUrl(request.getImageUrl());
        clothes.setUser(user);

        return clothesRepository.save(clothes);
    }

    public List<Clothes> getAllClothes() {
        return clothesRepository.findAll();
    }

    public List<Clothes> getClothesByUserId(Long userId) {
        return clothesRepository.findByUserId(userId);
    }

    public Clothes getClothesById(Long id) {
        return clothesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Clothes not found with id " + id));
    }

    public Clothes updateClothes(Long id, ClothesRequest request) {
        Clothes clothes = getClothesById(id);

        clothes.setName(request.getName());
        clothes.setCategory(request.getCategory());
        clothes.setColor(request.getColor());
        clothes.setImageUrl(request.getImageUrl());

        if (request.getUserId() != null) {
            User user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + request.getUserId()));
            clothes.setUser(user);
        }

        return clothesRepository.save(clothes);
    }

    @Transactional
    public void deleteClothes(Long id) {
        Clothes clothes = getClothesById(id);

        List<Outfit> affectedOutfits = outfitRepository.findByClothesId(id);

        for (Outfit outfit : affectedOutfits) {
            outfit.getClothes().removeIf(c -> c.getId().equals(id));
            outfitRepository.save(outfit);
        }

        clothesRepository.delete(clothes);
    }
}
