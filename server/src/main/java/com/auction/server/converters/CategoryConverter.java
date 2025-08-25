package com.auction.server.converters;

import com.auction.server.dtos.CategoryDto;
import com.auction.server.entities.Category;

public class CategoryConverter {

    public static Category toEntity(CategoryDto dto) {
        Category category = new Category();
        category.setName(dto.getName());
        return category;
    }

    public static CategoryDto toDto(Category entity) {
        CategoryDto dto = new CategoryDto();
        dto.setName(entity.getName());
        return dto;
    }
}
