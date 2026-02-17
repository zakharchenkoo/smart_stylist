package com.example.smart_stylist.repository;

import com.example.smart_stylist.entity.Clothes;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ClothesRepository extends JpaRepository<Clothes, Long> {
    List<Clothes> findByUserId(Long userId);
}
