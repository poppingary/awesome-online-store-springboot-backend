package com.gyl.awesome_inc.api;

import com.gyl.awesome_inc.domain.dto.AuthRequest;
import com.gyl.awesome_inc.domain.dto.AuthResponse;
import com.gyl.awesome_inc.domain.model.Customer;
import com.gyl.awesome_inc.service.CustomerService;
import com.gyl.awesome_inc.utility.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "api/auth/customer")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "http://localhost:3000", exposedHeaders = HttpHeaders.AUTHORIZATION)
public class AuthApi {
    private final AuthenticationManager authenticationManager;
    private final CustomerService customerService;
    private final JwtTokenUtil jwtTokenUtil;

    @PostMapping(value = "login")
    public ResponseEntity<?> login(@RequestBody @Valid AuthRequest authRequest) {
        String email = authenticateUserAndGetEmail(authRequest);

        Customer customer = customerService.getCustomerByEmail(email);
        if (!isSecurityQuestionAndAnswerCorrect(authRequest, customer)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String accessToken = jwtTokenUtil.generateAccessToken(customer);
        AuthResponse authResponse = new AuthResponse(customer.getId(), customer.getFirstName(), customer.getLastName());
        log.info("Log in: customerId={}, firstName={}, lastName={}", authResponse.getCustomerId(), authResponse.getFirstName(), authResponse.getLastName());

        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .body(authResponse);
    }

    private String authenticateUserAndGetEmail(AuthRequest authRequest) {
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
        ).getPrincipal().toString();
    }

    private boolean isSecurityQuestionAndAnswerCorrect(AuthRequest authRequest, Customer customer) {
        return customer.getSecurityQuestion().getSecurityQuestion().equals(authRequest.getSecurityQuestion()) &&
                customer.getSecurityAnswer().equals(authRequest.getSecurityAnswer());
    }
}