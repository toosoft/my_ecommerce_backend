package com.ndiamond.paintshop.controller;

import com.ndiamond.paintshop.dto.OrderDto;
import com.ndiamond.paintshop.exceptions.ResourceNotFoundException;
import com.ndiamond.paintshop.model.Order;
import com.ndiamond.paintshop.model.ShippingInformation;
import com.ndiamond.paintshop.model.User;
import com.ndiamond.paintshop.repository.OrderRepository;
import com.ndiamond.paintshop.request.CreateUserRequest;
import com.ndiamond.paintshop.request.ShippingInformationRequest;
import com.ndiamond.paintshop.response.ApiResponse;
import com.ndiamond.paintshop.service.ShippingInformation.IShippingInformationService;
import com.ndiamond.paintshop.service.order.IOrderService;
import com.ndiamond.paintshop.service.user.IUserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/orders")
public class OrderController {
    private final IOrderService orderService;
    private final IShippingInformationService shippingInformationService;
//    private final ShippingInformation shippingInformation;
    private final IUserService userService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllOrders() {
        try {
            List<OrderDto> order = orderService.getAllOrders();
            return ResponseEntity.ok((new ApiResponse("success!", order)));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("Ooops!", e.getMessage()));
        }
    }

    @PostMapping("/order")
    public ResponseEntity<ApiResponse> createOrder(
            @RequestBody ShippingInformationRequest request
    ) {
        try {
            User user = userService.getAuthenticatedUser();

            Map<String, Object> result =
                    orderService.placeOrderWithShipping(user.getId(), request);

            return ResponseEntity.ok(
                    new ApiResponse("Order + Shipping created successfully!", result)
            );

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Error occurred", e.getMessage()));
        }
    }

//
//    //place order not transactional with shipping
//    @PostMapping("/order")
//    public ResponseEntity<ApiResponse> createOrder( @RequestBody ShippingInformationRequest request)
//    {
//
//        try {
//            User user = userService.getAuthenticatedUser();
//            Long userId = user.getId();
//            Order order = orderService.placeOrder(userId);
//            Long orderId = order.getOrderId();
//
//            ShippingInformation saved = shippingInformationService.addShippingInformation(orderId, request);
//
//            OrderDto orderDto = orderService.convertToDto(order);
//
//            Map<String, Object> response = new HashMap<>();
//            response.put("order", orderDto);
//            response.put("shipping", saved);
//
//            return ResponseEntity.ok(
//                    new ApiResponse("Order + Shipping created successfully!", response)
//            );
//
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(new ApiResponse("Error occurred", e.getMessage()));
//        }
//    }


//    @PostMapping("/order")
//    public ResponseEntity<ApiResponse> createOrder(
////            @RequestParam Long userId,
//            @RequestBody ShippingInformation shippingInformation) {
//
//        try {
//            User user = userService.getAuthenticatedUser();
//            Long userId = user.getId();
//
//            // 1. Create order
//            Order order = orderService.placeOrder(userId);
//
//            // 2. Get generated orderId
//            Long orderId = order.getOrderId();
//
//            // 3. Save shipping info using orderId
//            ShippingInformation saved =
//                    shippingInformationService.addShippingInformation(orderId, shippingInformation);
//
//            // 4. Convert order to DTO
//            OrderDto orderDto = orderService.convertToDto(order);
//
//            // 5. Return both (optional)
//            Map<String, Object> response = new HashMap<>();
//            response.put("order", orderDto);
//            response.put("shipping", saved);
//
//            return ResponseEntity.ok(
//                    new ApiResponse("Order + Shipping created successfully!", response)
//            );
//
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(new ApiResponse("Error occurred", e.getMessage()));
//        }
//    }

//    @PostMapping("/order")
//    public ResponseEntity<ApiResponse> createOrder(@RequestParam Long userId) {
//        try {
//            Order order = orderService.placeOrder(userId);
//            OrderDto orderDto = orderService.convertToDto(order);
//
//            return ResponseEntity.ok(new ApiResponse("Item order success", orderDto));
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(new ApiResponse("Error occurred", e.getMessage()));
//        }
//    }



//    @PostMapping("/order")
//    public ResponseEntity<ApiResponse> createOrder(
    //            @RequestParam Long userId
//    ) {
//        try {
//            User user = userService.getAuthenticatedUser();
//            Long userId = user.getId();
//            Order order = orderService.placeOrder(userId);
//            OrderDto orderDto = orderService.convertToDto(order);
//
//            return ResponseEntity.ok(new ApiResponse("Item order success", orderDto));
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(new ApiResponse("Error occurred", e.getMessage()));
//        }
//    }


    @GetMapping("/order/{orderId}")
    public ResponseEntity<ApiResponse> getOrderById(@PathVariable Long orderId) {
        try {
            OrderDto order = orderService.getOrder(orderId);
            return ResponseEntity.ok((new ApiResponse("Item order success!", order)));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("Ooops!", e.getMessage()));
        }
    }


    @GetMapping("/user-orders")
    public ResponseEntity<ApiResponse> getUserOrders(
//            @PathVariable Long userId
    ) {
        try {
            User user = userService.getAuthenticatedUser();
            Long userId = user.getId();
            List<OrderDto> order = orderService.getUserOrders(userId);
            return ResponseEntity.ok((new ApiResponse("Item order success!", order)));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("Ooops!", e.getMessage()));
        }
    }

    //implement update order

    //implement delete order

}
