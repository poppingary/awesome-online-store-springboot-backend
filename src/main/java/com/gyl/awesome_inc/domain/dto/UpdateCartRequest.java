package com.gyl.awesome_inc.domain.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class UpdateCartRequest {
    @NotBlank
    private String productId;

    @NotBlank
    private String quantity;
}