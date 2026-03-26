package com.ndiamond.paintshop.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartItemDto {
//    private Long itemId;
//    private Integer quantity;
//    private ProductDto productDto;

    private Long productId;
    private BigDecimal unitPrice;
    private Integer quantity;
}
