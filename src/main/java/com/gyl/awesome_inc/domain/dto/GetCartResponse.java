package com.gyl.awesome_inc.domain.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GetCartResponse {
    private String productId;
    private String productName;
    private String quantity;
    private String unitPrice;
    private String discount;
}