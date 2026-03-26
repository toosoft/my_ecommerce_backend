package com.ndiamond.paintshop.dto;

import com.ndiamond.paintshop.model.Category;
import lombok.Data;

import java.util.List;

@Data
public class CategorySummaryDto {
    private Long id;
    private String name;
//    private int productCount;  // Just the count, not the full list
//
//    public CategorySummaryDto(Category category) {
//        this.id = category.getId();
//        this.name = category.getName();
//        this.productCount = category.getProducts() != null ? category.getProducts().size() : 0;
//    }
}
