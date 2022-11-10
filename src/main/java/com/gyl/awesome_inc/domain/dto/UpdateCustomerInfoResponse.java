package com.gyl.awesome_inc.domain.dto;

import com.gyl.awesome_inc.domain.model.SecurityQuestion;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdateCustomerInfoResponse {
    private String email;
    private String firstName;
    private String lastName;
    private String securityQuestion;
    private String securityAnswer;
}