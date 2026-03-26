package com.ndiamond.paintshop.service.order;

import com.ndiamond.paintshop.dto.OrderDto;
import com.ndiamond.paintshop.enums.OrderStatus;
import com.ndiamond.paintshop.exceptions.ResourceNotFoundException;
import com.ndiamond.paintshop.model.*;
import com.ndiamond.paintshop.repository.OrderRepository;
import com.ndiamond.paintshop.repository.ProductRepository;
import com.ndiamond.paintshop.repository.ShippingInformationRepository;
import com.ndiamond.paintshop.request.ShippingInformationRequest;
import com.ndiamond.paintshop.service.cart.CartService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final ShippingInformationRepository shippingInformationRepository;
    private final CartService cartService;
    private final ModelMapper modelMapper;

    @Transactional
    @Override
    public Order placeOrder(Long userId) {
        Cart cart = cartService.getCartByUserId(userId);
        Order order = createOrder(cart);
        List<OrderItem> orderItemList = createOrderItems(order, cart);
        order.setOrderItems(new HashSet<>(orderItemList));
        order.setTotalAmount(calculateTotalAmount(orderItemList));
        Order savedOrder = orderRepository.save(order);
        cartService.clearCart(cart.getId());

        return savedOrder;
    }

    @Transactional
    public Map<String, Object> placeOrderWithShipping(Long userId, ShippingInformationRequest request) {

        // 1. Create order
        Order order = placeOrder(userId);

        // 2. Create shipping
        ShippingInformation info = new ShippingInformation();
        info.setPhone(request.getPhone());
        info.setAddress(request.getAddress());
        info.setCity(request.getCity());
        info.setCountry(request.getCountry());

        info.setOrder(order);

        ShippingInformation saved = shippingInformationRepository.save(info);

        // 3. Convert
        OrderDto orderDto = convertToDto(order);

        // 4. Response
        Map<String, Object> response = new HashMap<>();
        response.put("order", orderDto);
        response.put("shipping", saved);

        return response;
    }


    private Order createOrder(Cart cart) {
        Order order = new Order();
        order.setUser(cart.getUser());
        order.setOrderStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDate.now());
        return order;

    }


    private List<OrderItem> createOrderItems(Order order, Cart cart) {
        return cart.getItems().stream().map(cartItem -> {
            Product product = cartItem.getProduct();
            product.setInventory(product.getInventory() - cartItem.getQuantity());
            productRepository.save(product);
            return new OrderItem(
                            order,
                            product,
                            cartItem.getQuantity(),
                            cartItem.getUnitPrice());
        }).toList();
    }


    private BigDecimal calculateTotalAmount(List<OrderItem> orderItemList) {
        return orderItemList.stream()
                .map(item -> item.getPrice()
                        .multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public OrderDto getOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .map(this::convertToDto)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found!"));
    }

    public List<OrderDto> getUserOrders(Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        return orders.stream().map(this::convertToDto).toList();
    }

    @Override
    public List<OrderDto> getAllOrders() {
        List<Order> OrderDto = orderRepository.findAll();
        return OrderDto.stream().map(this::convertToDto).toList();
    }

    @Override
    public OrderDto convertToDto(Order order) {
        return modelMapper.map(order, OrderDto.class);
    }

    @Override
    public Order getOrderById(Long orderId) {
        return null;
    }
}
