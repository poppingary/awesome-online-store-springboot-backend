package com.gyl.awesome_inc.domain.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdateCartResponse {
    private String productId;
    private String quantity;
}