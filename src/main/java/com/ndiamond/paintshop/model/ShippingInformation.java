package com.ndiamond.paintshop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ShippingInformation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long phone;
    private String address;
    private String city;
    private String country;


//    @OneToOne(mappedBy = "shippingInformation")
//    private Order order;

    @OneToOne
    @JoinColumn(name = "order_id")
//    @JsonIgnore
    private Order order;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
