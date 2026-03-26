package com.ndiamond.paintshop.controller;

import com.ndiamond.paintshop.dto.OrderDto;
import com.ndiamond.paintshop.response.ApiResponse;
import com.ndiamond.paintshop.service.checkout.ICheckoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/checkout")
@RequiredArgsConstructor
public class CheckoutController {

    private final ICheckoutService checkoutService;

    @PostMapping("/{userId}")
    public ResponseEntity<ApiResponse> checkout(@PathVariable Long userId) {

        OrderDto order = checkoutService.checkout(userId);

        return ResponseEntity.ok(
                new ApiResponse("Checkout successful", order)
        );
    }
}
