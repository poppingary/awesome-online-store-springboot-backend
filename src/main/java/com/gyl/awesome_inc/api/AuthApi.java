package com.gyl.awesome_inc.api;

import com.gyl.awesome_inc.domain.dto.AuthRequest;
import com.gyl.awesome_inc.domain.dto.AuthResponse;
import com.gyl.awesome_inc.domain.model.Customer;
import com.gyl.awesome_inc.service.CustomerService;
import com.gyl.awesome_inc.utility.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "api/customer")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:4200")
public class AuthApi {
    private final AuthenticationManager authenticationManager;
    private final CustomerService customerService;
    private final JwtTokenUtil jwtTokenUtil;

    @PostMapping(value = "login")
    public ResponseEntity<?> login(@RequestBody @Valid AuthRequest authRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
            );
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Customer customer = customerService.getCustomerByEmail(authRequest.getEmail());
        if (!(customer.getSecurityQuestion().getSecurityQuestion().equals(authRequest.getSecurityQuestion()) && customer.getSecurityAnswer().equals(authRequest.getSecurityAnswer()))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String accessToken = jwtTokenUtil.generateAccessToken(customer);
        AuthResponse authResponse = new AuthResponse(customer.getCustomerId(), customer.getFirstName(), customer.getLastName());

        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .body(authResponse);
    }
}