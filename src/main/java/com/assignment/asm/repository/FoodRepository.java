package com.assignment.asm.repository;

import com.assignment.asm.entity.Food;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FoodRepository extends JpaRepository<Food, Integer> {

    List<Food> findAllByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
            String name,
            String description
    );
}
