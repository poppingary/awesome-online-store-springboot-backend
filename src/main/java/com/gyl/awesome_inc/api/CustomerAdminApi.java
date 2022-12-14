package com.gyl.awesome_inc.api;

import com.gyl.awesome_inc.domain.dto.*;
import com.gyl.awesome_inc.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;

@RestController
@RequestMapping(path = "api/admin/customer")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000", exposedHeaders = HttpHeaders.AUTHORIZATION)
public class CustomerAdminApi {
    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<RegisterResponse> create(@RequestBody @Valid RegisterRequest registerRequest) {
        return customerService.create(registerRequest);
    }

    @GetMapping("{id}")
    public ResponseEntity<GetCustomerInfoResponse> get(@PathVariable String id) {
        return customerService.get(id);
    }

    @PutMapping("{id}")
    public ResponseEntity<UpdateCustomerInfoResponse> update(@PathVariable String id, @RequestBody @Valid UpdateCustomerInfoRequest updateCustomerInfoRequest) {
        return customerService.update(id, updateCustomerInfoRequest);
    }

    @PostMapping(value = "/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody @Valid ForgotPasswordRequest forgotPasswordRequest) throws MessagingException {
        return customerService.forgotPassword(forgotPasswordRequest);
    }

    @PostMapping(value = "/change-password")
    public ResponseEntity<?> changePassword(@RequestBody @Valid ChangePasswordRequest changePasswordRequest) {
        return customerService.changePassword(changePasswordRequest);
    }

    @PutMapping(value = "/update-password")
    public ResponseEntity<?> updatePassword(@RequestBody @Valid UpdatePasswordRequest updatePasswordRequest) {
        return customerService.updatePassword(updatePasswordRequest);
    }
}