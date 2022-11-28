package com.gyl.awesome_inc.domain.dto;

import lombok.Data;

@Data
public class UpdateCustomerInfoResponse {
    private String email;
    private String firstName;
    private String lastName;
    private String securityQuestion;
    private String securityAnswer;
}