package com.gyl.awesome_inc.domain.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class UpdatePasswordRequest {
    @NotBlank
    @Length(max = 50)
    private String customerId;

    @NotBlank
    private String oldPassword;

    @NotBlank
    private String newPassword;
}