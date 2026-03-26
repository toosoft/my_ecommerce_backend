package com.ndiamond.paintshop.request;

import lombok.Data;

@Data
public class ShippingInformationRequest {
    private Long phone;
    private String address;
    private String city;
    private String country;

}
