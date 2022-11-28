package com.gyl.awesome_inc.domain.dto;

import lombok.Data;

@Data
public class AddOrderResponse {
    private String orderId;
    private String arrivingDate;
}