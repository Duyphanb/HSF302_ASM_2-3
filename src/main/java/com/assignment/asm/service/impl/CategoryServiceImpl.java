package com.assignment.asm.service.impl;

import com.assignment.asm.entity.Category;
import com.assignment.asm.repository.CategoryRepository;
import com.assignment.asm.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository cateRepo;
    @Override
    public List<Category> getAllCates() {
        return cateRepo.findAll();
    }

    @Override
    public void createCategory(Category category) {
        cateRepo.save(category);
    }
}
