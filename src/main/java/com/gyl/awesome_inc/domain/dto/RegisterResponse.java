package com.gyl.awesome_inc.domain.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RegisterResponse {
    private String firstName;
    private String lastName;
    private String segment;
    private String email;
    private String securityQuestion;
    private String securityAnswer;
    private String market;
    private String region;
    private String country;
    private String state;
    private String city;
    private String postalCode;
    private String isPrimary;
}