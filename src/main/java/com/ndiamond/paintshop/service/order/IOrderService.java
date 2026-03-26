package com.ndiamond.paintshop.service.order;

import com.ndiamond.paintshop.dto.OrderDto;
import com.ndiamond.paintshop.model.Order;
import com.ndiamond.paintshop.request.ShippingInformationRequest;

import java.util.List;
import java.util.Map;

public interface IOrderService {
    Order placeOrder(Long userId);
    OrderDto getOrder(Long orderId);

    List<OrderDto> getUserOrders(Long userId);

    List<OrderDto> getAllOrders();

    OrderDto convertToDto(Order order);

    Order getOrderById(Long orderId);

    Map<String, Object> placeOrderWithShipping(Long id, ShippingInformationRequest request);
}
