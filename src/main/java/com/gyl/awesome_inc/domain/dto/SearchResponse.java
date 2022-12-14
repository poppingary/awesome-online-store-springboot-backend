package com.gyl.awesome_inc.domain.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SearchResponse {
    private String id;
    private String productName;
    private BigDecimal unitPrice;
    private BigDecimal discount;
    private Integer quantity;
    private String category;
    private String subcategory;
}
