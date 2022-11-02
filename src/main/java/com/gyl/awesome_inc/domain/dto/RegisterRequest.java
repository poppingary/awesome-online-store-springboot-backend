package com.gyl.awesome_inc.domain.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class RegisterRequest {
    @NotBlank
    @Length(max = 20)
    private String firstName;

    @NotBlank
    @Length(max = 20)
    private String lastName;

    @NotBlank
    @Length(max = 20)
    private String segment;

    @NotBlank
    @Email
    @Length(min = 10, max = 50)
    private String email;

    @NotBlank
    @Length(min = 8, max = 20)
    private String password;

    @NotBlank
    @Length(max = 100)
    private String securityQuestion;

    @NotBlank
    @Length(max = 100)
    private String securityAnswer;

    @NotBlank
    @Length(max = 20)
    private String market;

    @NotBlank
    @Length(max = 20)
    private String region;

    @NotBlank
    @Length(max = 50)
    private String country;

    @NotBlank
    @Length(max = 50)
    private String state;

    @NotBlank
    @Length(max = 50)
    private String city;

    @Length(max = 10)
    private String postalCode;

    @NotBlank
    @Length(max = 1)
    private String isPrimary;
}