package com.gyl.awesome_inc.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor()
public class AuthResponse implements Serializable {
    private String customerId;
    private String firstName;
    private String lastName;
}
