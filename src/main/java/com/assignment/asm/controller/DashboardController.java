package com.assignment.asm.controller;

import com.assignment.asm.service.CategoryService;
import com.assignment.asm.service.FoodService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final FoodService foodService;
    private final CategoryService categoryService;

    public DashboardController(FoodService foodService, CategoryService categoryService) {
        this.foodService = foodService;
        this.categoryService = categoryService;
    }

    @GetMapping("/")
    public String dashboard(Model model) {
        model.addAttribute("foodCount", foodService.getAllFood().size());
        model.addAttribute("categoryCount", categoryService.getAllCates().size());
        return "dashboard";
    }
}
