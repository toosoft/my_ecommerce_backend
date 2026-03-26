package com.ndiamond.paintshop.controller;


import com.ndiamond.paintshop.exceptions.ResourceNotFoundException;
import com.ndiamond.paintshop.model.ShippingInformation;
import com.ndiamond.paintshop.request.ShippingInformationRequest;
import com.ndiamond.paintshop.response.ApiResponse;
import com.ndiamond.paintshop.service.ShippingInformation.IShippingInformationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/shipping")
public class ShippingInformationController {

    private final IShippingInformationService shippingInformationService;

    // ✅ Add shipping info to an order
    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addShippingInformation(
            @RequestParam Long orderId,
            @RequestBody ShippingInformationRequest request

    ) {

        try {
            ShippingInformation saved = shippingInformationService.addShippingInformation(orderId, request);
            return ResponseEntity.ok(new ApiResponse("Shipping information added successfully!", saved)
            );

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Error occurred", e.getMessage()));
        }
    }

    // ✅ Get shipping info by order
    @GetMapping("/order/{orderId}")
    public ResponseEntity<ApiResponse> getOrderShippingInformation(
            @PathVariable Long orderId) {

        try {
            ShippingInformation info =
                    shippingInformationService.getOrderShippingInformation(orderId);

            return ResponseEntity.ok(
                    new ApiResponse("Success!", info)
            );

        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("Ooops!", e.getMessage()));
        }
    }

    // ✅ Get all shipping info
    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllShippingInformation() {

        try {
            List<ShippingInformation> list = shippingInformationService.getAllShippingInformation();
            return ResponseEntity.ok(new ApiResponse("Success!", list)
            );

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Error occurred", e.getMessage()));
        }
    }

    // ✅ Get shipping info by user
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse> getUserShippingInformation(
            @PathVariable Long userId) {

        try {
            List<ShippingInformation> list =
                    shippingInformationService.getUserShippingInformation(userId);

            return ResponseEntity.ok(
                    new ApiResponse("Success!", list)
            );

        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("Ooops!", e.getMessage()));
        }
    }
}
