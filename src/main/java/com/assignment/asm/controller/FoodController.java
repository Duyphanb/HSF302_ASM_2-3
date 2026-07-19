package com.assignment.asm.controller;


import com.assignment.asm.entity.Food;
import com.assignment.asm.service.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/food/donuong")
// localhots:8080/food/donuong
public class FoodController {

    @Autowired
    private FoodService foodService;

    @GetMapping
    public String getAllFood(Model box){

        List<Food> foods = foodService.getAllFood();

        box.addAttribute("foodbags", foods);

        return "food/list";
    }
}
