package com.ndiamond.paintshop.service.checkout;

import com.ndiamond.paintshop.dto.OrderDto;
import jakarta.transaction.Transactional;

public interface ICheckoutService {
    @Transactional
    OrderDto checkout(Long userId);
}
