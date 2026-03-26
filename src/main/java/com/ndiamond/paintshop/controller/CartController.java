package com.ndiamond.paintshop.controller;


import com.ndiamond.paintshop.dto.CartDto;
import com.ndiamond.paintshop.exceptions.ResourceNotFoundException;
import com.ndiamond.paintshop.model.Cart;
import com.ndiamond.paintshop.model.User;
import com.ndiamond.paintshop.response.ApiResponse;
import com.ndiamond.paintshop.service.cart.ICartService;
import com.ndiamond.paintshop.service.user.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/carts")
public class CartController {
    private final ICartService cartService;
    private final IUserService userService;

//    @GetMapping("/{cartId}/my-cart")
//@GetMapping("/{userId}/user-cart")
    @GetMapping("/user-cart")
    public ResponseEntity<ApiResponse> getCart(
//            @PathVariable Long userId
    ) {
        try {
//            User user = userService.getUserById(userId);
            User user = userService.getAuthenticatedUser();
            Cart cart = user.getCart();

            if (cart == null) {
                return ResponseEntity.ok(new ApiResponse("cart not exist", null));
            }

            Long cartId = cart.getId();
            CartDto cartDto = cartService.getCartDto(cartId);
            return ResponseEntity.ok(new ApiResponse("Success", cartDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/clear")
    public ResponseEntity<ApiResponse> clearCart() {
        try {
            User user = userService.getAuthenticatedUser();
            Cart cart = user.getCart();

            if (cart == null) {
                return ResponseEntity.ok(new ApiResponse("cart not exist", null));
            }

            Long cartId = cart.getId();
            cartService.clearCart(cartId);
            return ResponseEntity.ok(new ApiResponse("Clear Cart Success!", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/cart/{cartId}/total-price")
    public ResponseEntity<ApiResponse> getTotalAmount(@PathVariable Long cartId) {
        try {
            BigDecimal totalPrice = cartService.getTotalPrice(cartId);
            return ResponseEntity.ok(new ApiResponse("Total Price", totalPrice));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }

    }
}
