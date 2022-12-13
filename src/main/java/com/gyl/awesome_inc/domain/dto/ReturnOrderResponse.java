package com.gyl.awesome_inc.domain.dto;

import com.gyl.awesome_inc.domain.model.Order;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReturnOrderResponse {
    private Order order;
}