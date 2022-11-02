package com.gyl.awesome_inc.domain.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import java.io.Serializable;

@Getter
@Setter
public class AuthRequest implements Serializable {
    @NotNull
    @Email
    @Length(min = 5, max = 50)
    private String email;

    @NotNull
    @Length(min = 5, max = 20)
    private String password;

    @NotNull
    private String securityQuestion;

    @NotNull
    private String securityAnswer;
}
