package com.gyl.awesome_inc.domain.dto;

import lombok.Data;

@Data
public class CreateAddressResponse {
    private String shipAddressId;
    private String postalCode;
    private String city;
    private String state;
    private String country;
    private String region;
    private String market;
    private String isPrimary;
}