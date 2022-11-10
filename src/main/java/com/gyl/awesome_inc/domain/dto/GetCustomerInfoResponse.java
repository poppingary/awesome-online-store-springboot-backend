package com.gyl.awesome_inc.domain.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GetCustomerInfoResponse {
    private String email;
    private String firstName;
    private String lastName;
    private String securityQuestion;
    private String securityAnswer;
}