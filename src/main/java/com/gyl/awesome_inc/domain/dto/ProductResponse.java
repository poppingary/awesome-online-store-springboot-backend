package com.gyl.awesome_inc.domain.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductResponse {
    private String productName;
    private Integer quantity;
    private BigDecimal actualPrice;
}
