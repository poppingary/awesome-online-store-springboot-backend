package com.gyl.awesome_inc.domain.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class UpdateAddressRequest {
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
    @Length(max = 1)
    private String isPrimary;
}