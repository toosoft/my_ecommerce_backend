package com.ndiamond.paintshop.controller;

import com.ndiamond.paintshop.dto.CartItemDto;
import com.ndiamond.paintshop.exceptions.ResourceNotFoundException;
import com.ndiamond.paintshop.model.Cart;
import com.ndiamond.paintshop.model.User;
import com.ndiamond.paintshop.request.CartItemRequest;
import com.ndiamond.paintshop.response.ApiResponse;
import com.ndiamond.paintshop.service.cart.ICartItemService;
import com.ndiamond.paintshop.service.cart.ICartService;
import com.ndiamond.paintshop.service.user.IUserService;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/cartItems")
public class CartItemController {
    private final ICartItemService cartItemService;
    private final ICartService cartService;
    private final IUserService userService;

    @PostMapping("/item/add")
    public ResponseEntity<ApiResponse> addItemToCart(
                                                     @RequestParam Long productId,
                                                     @RequestParam Integer quantity) {
        try {

            User user = userService.getAuthenticatedUser();
            Cart cart = cartService.initializeNewCart(user);

            cartItemService.addItemToCart(cart.getId(), productId, quantity);
            return ResponseEntity.ok(new ApiResponse("Add item success", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        } catch (JwtException e) {
            return ResponseEntity.status(UNAUTHORIZED).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PostMapping("/items/add")
    public ResponseEntity<ApiResponse> addItemsToCart(
            @RequestBody List<CartItemRequest> items) {

        try {
            User user = userService.getAuthenticatedUser();
            Cart cart = cartService.initializeNewCart(user);

            cartItemService.addItemsToCart(cart.getId(), items);

            return ResponseEntity.ok(new ApiResponse("Items added successfully", null));

        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));

        } catch (JwtException e) {
            return ResponseEntity.status(UNAUTHORIZED)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

//    @PostMapping("/cart/merge")
//    public ResponseEntity<ApiResponse> mergeCart(@RequestBody CartItemRequest request) {
//        try {
//            User user = userService.getAuthenticatedUser();
//            Cart cart = cartService.initializeNewCart(user);
//
//            // Loop through items from client
//            for (CartItemDto item : request.getCartItems()) {
//                // Add item if new, update quantity if exists
//                cartItemService.addOrUpdateItem(cart.getId(), item.getProductId(), item.getQuantity());
//            }
//
//            return ResponseEntity.ok(new ApiResponse("Cart merged successfully", null));
//        } catch (ResourceNotFoundException e) {
//            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
//        } catch (JwtException e) {
//            return ResponseEntity.status(UNAUTHORIZED).body(new ApiResponse(e.getMessage(), null));
//        }
//    }

    @DeleteMapping("/item/decrease/{productId}")
    public ResponseEntity<ApiResponse> decreaseCartItem(@PathVariable Long productId) {
        try {
            User user = userService.getAuthenticatedUser();

            cartItemService.decreaseItemQuantity(user.getId(), productId);

            return ResponseEntity.ok(
                    new ApiResponse("Cart item updated successfully", null)
            );

        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Error occurred", e.getMessage()));
        }
    }

    @DeleteMapping("/{cartId}/remove/{productId}")
    public ResponseEntity<ApiResponse> removeItemFromCart(@PathVariable Long cartId, @PathVariable Long productId) {
        try {
            cartItemService.removeItemFromCart(cartId, productId);
            return ResponseEntity.ok(new ApiResponse("Remove item success", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }


    @PutMapping("/cart/{cartId}/item/{itemId}/update")
    public ResponseEntity<ApiResponse> updateItemQuantity(@PathVariable Long cartId,
                                                          @PathVariable Long itemId,
                                                          @RequestParam Integer quantity) {
        try {

            cartItemService.updateItemQuantity(cartId, itemId, quantity);
            return ResponseEntity.ok(new ApiResponse("Update Item Success", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
