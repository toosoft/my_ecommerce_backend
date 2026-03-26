package com.ndiamond.paintshop.service.category;

import com.ndiamond.paintshop.dto.CategoryDto;
import com.ndiamond.paintshop.model.Category;

import java.util.List;

public interface ICategoryService {
    Category getCategoryById(Long id);
    Category getCategoryByName(String name);
    List<Category> getAllCategories();
    Category addCategory(Category category);
    Category updateCategory(Category category, Long id);
    void deleteCategoryById(Long id);

    List<CategoryDto> getConvertedCategories(List<Category> Categories);

    CategoryDto convertToDto(Category category);
}
