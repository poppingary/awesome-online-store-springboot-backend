package com.gyl.awesome_inc.domain.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
public class AddOrderRequest {
    @NotBlank
    @Length(max = 50)
    private String customerId;

    @NotBlank
    @Length(max = 30)
    private String shipMode;

    @Length(max = 10)
    private String postalCode;

    @Length(max = 50)
    private String city;

    @Length(max = 50)
    private String state;

    @Length(max = 50)
    private String country;

    @NotBlank
    @Length(max = 20)
    private String region;

    @NotBlank
    @Length(max = 20)
    private String market;

    @NotBlank
    @Length(max = 30)
    private String orderPriority;

    @NotBlank
    @Length(max = 1)
    private String isReturned;

    @NotBlank
    @Length(max = 50)
    private String creditCardHolder;

    @NotBlank
    @Length(max = 30)
    private String creditCardNumber;

    @NotBlank
    @Length(max = 10)
    private String creditCardExpiredDate;

    @NotBlank
    @Length(max = 3)
    private String creditCardCvv;

    private List<ProductQuantity> productQuantityList;
}