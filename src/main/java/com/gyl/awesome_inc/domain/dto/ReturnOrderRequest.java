package com.gyl.awesome_inc.domain.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class ReturnOrderRequest {
    @Length(max = 1)
    private String isReturned;
}