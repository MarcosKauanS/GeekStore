package com.geekstore.geekstore.modules.admin.controller;

import com.geekstore.geekstore.modules.category.model.Category;
import com.geekstore.geekstore.modules.category.service.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/categories")
public class AdminCategoryController {

    private final CategoryService categoryService;

    public AdminCategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public String index(Model model) {
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("category", new Category()); // Para o formulário modal
        return "admin/category/index";
    }

    @PostMapping
    public String save(@ModelAttribute Category category, Model model) {
        if (category.getId() != null) {
            // Edição
            categoryService.save(category);
        } else {
            // Nova categoria
            if (!categoryService.existsByName(category.getName())) {
                categoryService.save(category);
            } else {
                model.addAttribute("errorMessage", "Categoria já existe!");
                model.addAttribute("categories", categoryService.findAll());
                model.addAttribute("category", category);
                return "admin/category/index";
            }
        }
        return "redirect:/admin/categories";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        categoryService.delete(id);
        return "redirect:/admin/categories";
    }
}
