package com.itassettracker.it_asset_tracker.service;

import com.itassettracker.it_asset_tracker.dto.*;
import com.itassettracker.it_asset_tracker.entity.Category;
import com.itassettracker.it_asset_tracker.exception.ResourceNotFoundException;
import com.itassettracker.it_asset_tracker.repository.CategoryRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {
    private final CategoryRepository categories;
    public CategoryService(CategoryRepository categories) { this.categories = categories; }
    public List<CategoryResponse> all() { return categories.findAll().stream().map(c -> new CategoryResponse(c.getId(), c.getName())).toList(); }
    public CategoryResponse create(CategoryRequest request) {
        String name = request.getName().trim();
        if (categories.existsByNameIgnoreCase(name)) throw new IllegalArgumentException("Category name already exists.");
        Category saved = categories.save(new Category(name));
        return new CategoryResponse(saved.getId(), saved.getName());
    }
    public Category get(Long id) { return categories.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category not found: " + id)); }
}
