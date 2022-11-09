package com.gyl.awesome_inc.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ForgotPasswordResponse {
    private String email;
    private String subject;
    private String text;
}