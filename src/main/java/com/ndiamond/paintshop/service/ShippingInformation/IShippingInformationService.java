package com.ndiamond.paintshop.service.ShippingInformation;

import com.ndiamond.paintshop.dto.ShippingInformationDto;
import com.ndiamond.paintshop.model.ShippingInformation;
import com.ndiamond.paintshop.request.ShippingInformationRequest;

import java.util.List;

public interface IShippingInformationService {
    ShippingInformation addShippingInformation(Long orderId, ShippingInformationRequest info);

    ShippingInformation getOrderShippingInformation(Long orderId);
    List<ShippingInformation> getUserShippingInformation(Long userId);

    List<ShippingInformation> getAllShippingInformation();

//    ShippingInformationDto convertToDto(ShippingInformation shippingInformation);

}
