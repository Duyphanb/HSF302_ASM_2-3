package com.assignment.asm.service;

import com.assignment.asm.entity.Food;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
@Service
public interface FoodService {
    public List<Food> getAllFood();

    public Food createFood(Food food, MultipartFile image);


    public Food getFoodByName(Integer id);


    public void updateFood(Integer id, Food food);


    public void deleteFoodById(Integer id);


    public List<Food> searchFood(String keyword);
}
