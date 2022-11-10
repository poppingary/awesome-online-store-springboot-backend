package com.gyl.awesome_inc.domain.dto;

import com.gyl.awesome_inc.domain.model.SecurityQuestion;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class UpdateCustomerInfoRequest {
    @NotBlank
    @Email
    @Length(min = 10, max = 50)
    private String email;

    @NotBlank
    @Length(max = 20)
    private String firstName;

    @NotBlank
    @Length(max = 20)
    private String lastName;

    @NotBlank
    @Length(max = 100)
    private String securityQuestion;

    @NotBlank
    @Length(max = 100)
    private String securityAnswer;
}