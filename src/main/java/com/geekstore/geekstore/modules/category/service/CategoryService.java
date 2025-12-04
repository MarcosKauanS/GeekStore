package com.geekstore.geekstore.modules.category.service;

import com.geekstore.geekstore.modules.category.model.Category;
import com.geekstore.geekstore.modules.category.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository repository;

    public CategoryService(CategoryRepository repository) {
        this.repository = repository;
    }

    public List<Category> findAll() {
        return repository.findAll();
    }

    public Optional<Category> findById(Long id) {
        return repository.findById(id);
    }

    public Category save(Category category) {
        return repository.save(category);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public boolean existsByName(String name) {
        return repository.existsByName(name);
    }

    public Optional<Category> findByName(String name) {
        return repository.findByName(name);
    }
}
