package com.gyl.awesome_inc.domain.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Getter
@Setter
public class AuthRequest implements Serializable {
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
}
