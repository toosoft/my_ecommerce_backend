package com.ndiamond.paintshop.service.cart;

import com.ndiamond.paintshop.dto.CartDto;
import com.ndiamond.paintshop.model.Cart;
import com.ndiamond.paintshop.model.User;

import java.math.BigDecimal;

public interface ICartService {
    Cart getCart(Long id);

    CartDto getCartDto(Long id);

    void clearCart(Long id);
    BigDecimal getTotalPrice(Long id);

    Cart initializeNewCart(User user);

    Cart getCartByUserId(Long userId);

}
