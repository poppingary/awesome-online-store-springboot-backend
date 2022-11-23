package com.gyl.awesome_inc.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddToCartResponse {
    private String productId;
    private String quantity;
}