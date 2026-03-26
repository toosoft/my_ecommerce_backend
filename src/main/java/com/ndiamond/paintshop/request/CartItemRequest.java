package com.ndiamond.paintshop.request;

import com.ndiamond.paintshop.dto.CartItemDto;
import com.ndiamond.paintshop.dto.ProductDto;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CartItemRequest {
//    private List<CartItemDto> items; // review concept

//    private Long itemId;
    private Long productId;
    private Integer quantity;
//    private BigDecimal unitPrice;

}