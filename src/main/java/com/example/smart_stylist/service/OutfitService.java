package com.example.smart_stylist.service;

import com.example.smart_stylist.dto.OutfitRequest;
import com.example.smart_stylist.entity.Clothes;
import com.example.smart_stylist.entity.Outfit;
import com.example.smart_stylist.entity.User;
import com.example.smart_stylist.exception.ResourceNotFoundException;
import com.example.smart_stylist.repository.ClothesRepository;
import com.example.smart_stylist.repository.OutfitRepository;
import com.example.smart_stylist.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OutfitService {

    private final OutfitRepository outfitRepository;
    private final UserRepository userRepository;
    private final ClothesRepository clothesRepository;

    public OutfitService(OutfitRepository outfitRepository, UserRepository userRepository, ClothesRepository clothesRepository) {
        this.outfitRepository = outfitRepository;
        this.userRepository = userRepository;
        this.clothesRepository = clothesRepository;
    }

    public Outfit createOutfit(OutfitRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + request.getUserId()));

        List<Clothes> clothesList = clothesRepository.findAllById(request.getClothesIds());

        if (clothesList.size() != request.getClothesIds().size()) {
            throw new ResourceNotFoundException("One or more clothes items not found");
        }

        Outfit outfit = new Outfit();
        outfit.setName(request.getName());
        outfit.setClothes(clothesList);
        outfit.setUser(user);

        return outfitRepository.save(outfit);
    }

    public List<Outfit> getAllOutfits() {
        return outfitRepository.findAll();
    }

    public Outfit getOutfitById(Long id) {
        return outfitRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Outfit not found with id " + id));
    }

    public List<Outfit> getOutfitsByUserId(Long userId) {
        return outfitRepository.findByUserId(userId);
    }

    public Outfit updateOutfit(Long id, OutfitRequest request) {
        Outfit outfit = getOutfitById(id);

        outfit.setName(request.getName());

        List<Clothes> clothesList = clothesRepository.findAllById(request.getClothesIds());
        if (clothesList.size() != request.getClothesIds().size()) {
            throw new ResourceNotFoundException("One or more clothes items not found");
        }
        outfit.setClothes(clothesList);

        return outfitRepository.save(outfit);
    }

    public void deleteOutfit(Long id) {
        if (!outfitRepository.existsById(id)) {
            throw new ResourceNotFoundException("Outfit not found with id " + id);
        }
        outfitRepository.deleteById(id);
    }
}
