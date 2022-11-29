package com.gyl.awesome_inc.domain.dto;

import lombok.Data;

import java.util.Set;

@Data
public class GetOrderResponse {
    private Set<OrderResponse> orderSet;
}