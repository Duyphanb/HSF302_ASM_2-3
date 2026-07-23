package com.assignment.asm.service.impl;

import com.assignment.asm.entity.Food;
import com.assignment.asm.repository.FoodRepository;
import com.assignment.asm.service.FoodService;
import com.assignment.asm.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class FoodServiceImpl implements FoodService {
    @Autowired
    private FoodRepository foodRepo;

    @Autowired
    private ImageService imageService;

    @Override
    public List<Food> getAllFood() {
        return foodRepo.findAll();
    }

    @Override
    public Food createFood(Food food, MultipartFile image) {
        if(image != null && !image.isEmpty()){


            String imageUrl =
                    imageService.upload(image);


            food.setImageUrl(imageUrl);

        }


        return foodRepo.save(food);
    }



    @Override
    public Food getFoodByName(Integer id) {
        return foodRepo.findById(id).orElse(null);
    }

    @Override
    public void updateFood(Integer id, Food food) {
        foodRepo.save(food);
    }

    @Override
    public void deleteFoodById(Integer id) {
        foodRepo.deleteById(id);
    }

    @Override
    public List<Food> searchFood(String keyword) {
        String kw = keyword.trim();
        if(kw.isBlank()){
            return foodRepo.findAll();
        }
        return foodRepo.findAllByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(kw, kw);
    }
}
