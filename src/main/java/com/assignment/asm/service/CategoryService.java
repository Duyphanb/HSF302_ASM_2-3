package com.assignment.asm.service;

import com.assignment.asm.entity.Category;

import java.util.List;

public interface CategoryService {

    public List<Category> getAllCates();



    public void createCategory(Category category);
}
