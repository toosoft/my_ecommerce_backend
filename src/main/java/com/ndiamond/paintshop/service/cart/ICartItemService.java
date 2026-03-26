package com.ndiamond.paintshop.service.cart;

import com.ndiamond.paintshop.model.CartItem;
import com.ndiamond.paintshop.request.CartItemRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ICartItemService {
    void addItemToCart(Long cartId, Long productId, int quantity);

    @Transactional
    void addItemsToCart(Long cartId, List<CartItemRequest> items);

    void removeItemFromCart(Long cartId, Long productId);
    void updateItemQuantity(Long cartId, Long productId, int quantity);

    CartItem getCartItem(Long cartId, Long productId);

    @Transactional
    void decreaseItemQuantity(Long userId, Long productId);
}
