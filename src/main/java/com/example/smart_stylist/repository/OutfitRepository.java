package com.example.smart_stylist.repository;

import com.example.smart_stylist.entity.Outfit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OutfitRepository extends JpaRepository<Outfit, Long> {
    List<Outfit> findByUserId(Long userId);
    @Query("SELECT o FROM Outfit o JOIN o.clothes c WHERE c.id = :clothesId")
    List<Outfit> findByClothesId(@Param("clothesId") Long clothesId);
}
