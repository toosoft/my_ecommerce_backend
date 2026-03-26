package com.ndiamond.paintshop.service.ShippingInformation;

import com.ndiamond.paintshop.model.Order;
import com.ndiamond.paintshop.model.ShippingInformation;
import com.ndiamond.paintshop.model.User;
import com.ndiamond.paintshop.repository.ShippingInformationRepository;
import com.ndiamond.paintshop.repository.UserRepository;
import com.ndiamond.paintshop.request.ShippingInformationRequest;
import com.ndiamond.paintshop.service.order.IOrderService;
import com.ndiamond.paintshop.service.user.UserService;
import lombok.*;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class ShippingInformationService implements IShippingInformationService {

    private final IOrderService orderService;
    private final ShippingInformationRepository shippingInformationRepository;
    private final UserService userService;
    private final UserRepository userRepository;


    @Override
    public ShippingInformation addShippingInformation(Long orderId, ShippingInformationRequest request) {

        Order order = orderService.getOrderById(orderId);

        ShippingInformation info = new ShippingInformation();

        info.setPhone(request.getPhone());
        info.setAddress(request.getAddress());
        info.setCity(request.getCity());
        info.setCountry(request.getCountry());

        // 🔥 VERY IMPORTANT
        info.setOrder(order);

        return shippingInformationRepository.save(info);
    }



    @Override
    public ShippingInformation getOrderShippingInformation(Long orderId) {
        return orderService.getOrderById(orderId).getShippingInformation();
    }

    @Override
    public List<ShippingInformation> getUserShippingInformation(Long userId) {
        return List.of();
    }

    @Override
    public List<ShippingInformation> getAllShippingInformation() {
        return shippingInformationRepository.findAll();
    }

//    @Override
//    public List<ShippingInformation> getUserShippingInformation(Long userId) {
//        return shippingInformationRepository.findByUser_Id(userId);
//    }
}