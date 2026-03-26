package com.ndiamond.paintshop.controller;


import com.ndiamond.paintshop.dto.CategoryDto;
import com.ndiamond.paintshop.exceptions.AlreadyExistsException;
import com.ndiamond.paintshop.exceptions.ResourceNotFoundException;
import com.ndiamond.paintshop.model.Category;
import com.ndiamond.paintshop.response.ApiResponse;
import com.ndiamond.paintshop.service.category.ICategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/categories")
public class CategoryController {
    private final ICategoryService categoryService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllCategories(){
        try {
            List<Category> categories = categoryService.getAllCategories();
            List<CategoryDto> categoryDto = categoryService.getConvertedCategories(categories);

            return ResponseEntity.ok(new ApiResponse("Found!", categoryDto));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Error:", INTERNAL_SERVER_ERROR));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addCategory(@RequestBody Category name){
        try {
            Category theCategory = categoryService.addCategory(name);
            CategoryDto categoryDto = categoryService.convertToDto(theCategory);
            return ResponseEntity.ok(new ApiResponse("Success", categoryDto));
        } catch (AlreadyExistsException e) {
           return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/category/{id}/category")
    public ResponseEntity<ApiResponse> getCategoryById (@PathVariable Long id) {
        try {
            Category theCategory = categoryService.getCategoryById(id);
            CategoryDto categoryDto = categoryService.convertToDto(theCategory);

            return ResponseEntity.ok(new ApiResponse("Found", categoryDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("category/{name}/category")
    public ResponseEntity<ApiResponse> getCategoryByName (@PathVariable String name) {
        try {
            Category theCategory = categoryService.getCategoryByName(name);
            CategoryDto categoryDto = categoryService.convertToDto(theCategory);

            return ResponseEntity.ok(new ApiResponse("Found", categoryDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/category/{id}/delete")
    public ResponseEntity<ApiResponse> deleteCategory (@PathVariable Long id) {
        try {
            categoryService.deleteCategoryById(id);
            return ResponseEntity.ok(new ApiResponse("Found", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/category/{id}/update")
    public ResponseEntity<ApiResponse> updateCategory(@PathVariable Long id, @RequestBody Category category) {
        try {
            Category updatedCategory = categoryService.updateCategory(category, id);
            CategoryDto categoryDto = categoryService.convertToDto(updatedCategory);

            return ResponseEntity.ok(new ApiResponse("Update success!", categoryDto));
        } catch (ResourceNotFoundException e){
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }

    }
}
