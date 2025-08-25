package com.auction.server.services;

import com.auction.server.converters.CategoryConverter;
import com.auction.server.dtos.CategoryDto;
import com.auction.server.entities.Category;
import com.auction.server.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Optional<Category> getCategoryById(Integer id) {
        return categoryRepository.findById(id);
    }

    public void createCategory(CategoryDto dto) {
        Category category = CategoryConverter.toEntity(dto);
        categoryRepository.save(category);
    }

    public void deleteCategory(Integer id) {
        categoryRepository.deleteById(id);
    }
}
