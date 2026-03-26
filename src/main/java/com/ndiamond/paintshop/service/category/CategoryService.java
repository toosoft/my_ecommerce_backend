package com.ndiamond.paintshop.service.category;

import com.ndiamond.paintshop.dto.CategoryDto;
import com.ndiamond.paintshop.exceptions.AlreadyExistsException;
import com.ndiamond.paintshop.exceptions.ResourceNotFoundException;
import com.ndiamond.paintshop.model.Category;
import com.ndiamond.paintshop.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;


    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Category not found!"));
    }

    @Override
    public Category getCategoryByName(String name) {
        return categoryRepository.findByName(name);
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category addCategory(Category category) {
        return Optional.of(category).filter(c-> !categoryRepository.existsByName(c.getName()))
                .map(categoryRepository::save)
                .orElseThrow(()-> new AlreadyExistsException(category.getName()+ "already exists"));
    }

    @Override
    public Category updateCategory(Category category, Long id) {
        return Optional.ofNullable(getCategoryById(id)).map(oldCategory -> {
                    oldCategory.setName(category.getName());
                    return categoryRepository.save(oldCategory);
                }).orElseThrow(()-> new ResourceNotFoundException("Category not found!"));
    }

    @Override
    public void deleteCategoryById(Long id) {
        categoryRepository.findById(id).ifPresentOrElse(categoryRepository::delete, () -> {
            throw  new ResourceNotFoundException("Category not found!");
        });
    }

    @Override
    public List<CategoryDto> getConvertedCategories(List<Category> Categories) {
        return Categories.stream().map(this::convertToDto).toList();
    }

    @Override
    public CategoryDto convertToDto(Category category) {
        return modelMapper.map(category, CategoryDto.class);
    }

}
