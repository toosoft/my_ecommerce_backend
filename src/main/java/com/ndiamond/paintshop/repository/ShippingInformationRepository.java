package com.ndiamond.paintshop.repository;

import com.ndiamond.paintshop.model.Order;
import com.ndiamond.paintshop.model.ShippingInformation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShippingInformationRepository extends JpaRepository<ShippingInformation, Long> {

}
