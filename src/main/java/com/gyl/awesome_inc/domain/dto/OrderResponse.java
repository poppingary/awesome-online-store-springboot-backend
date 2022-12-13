package com.gyl.awesome_inc.domain.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;

@Data
public class OrderResponse {
    private String id;
    private String postalCode;
    private String city;
    private String state;
    private String country;
    private String region;
    private String market;
    private String shipMode;
    private String orderPriority;
    private String isReturned;
    private Instant orderDate;
    private BigDecimal totalPrice;
    private String arrivingDate;
    private Set<ProductInOrderResponse> productInOrderResponseSet;
}