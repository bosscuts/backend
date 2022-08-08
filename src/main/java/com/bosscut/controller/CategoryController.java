package com.bosscut.controller;

import com.bosscut.entity.Category;
import com.bosscut.repository.CategoryRepository;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/category")
public class CategoryController {
    private final CategoryRepository categoryRepository;

    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @GetMapping({"/"})
    @Transactional
    public String list(Model model) {
        List<Category> categories = categoryRepository.findAll();
        model.addAttribute("categories", categories);
        return "frontend/category/index";
    }

    @GetMapping({"/register"})
    @Transactional
    public String register() {
        return "frontend/category/register";
    }

    @PostMapping
    @Transactional
    public String createUrl(@RequestParam String url, @RequestParam String name) {
        Category entity = new Category();
        entity.setName(name);
        entity.setUrl(url);
        categoryRepository.save(entity);
        return "redirect:/category/";
    }

    @GetMapping({"/delete/{categoryId}"})
    @Transactional
    public String deleteCategory(@PathVariable Long categoryId) {
        Category entity = categoryRepository.findById(categoryId).orElse(null);
        if (Objects.nonNull(entity)) {
            categoryRepository.delete(entity);
        }
        return "redirect:/category/";
    }
}
