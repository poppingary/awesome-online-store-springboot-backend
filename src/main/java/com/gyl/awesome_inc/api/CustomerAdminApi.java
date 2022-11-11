package com.gyl.awesome_inc.api;

import com.gyl.awesome_inc.domain.dto.*;
import com.gyl.awesome_inc.domain.model.Customer;
import com.gyl.awesome_inc.service.CustomerService;
import com.gyl.awesome_inc.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;

@RestController
@RequestMapping(path = "api/admin/customer")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:3000")
public class CustomerAdminApi {
    private final CustomerService customerService;
    private final EmailService emailService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody @Valid RegisterRequest registerRequest) {
        return customerService.create(registerRequest);
    }

    @GetMapping("{id}")
    public ResponseEntity<?> get(@PathVariable String id) {
        return customerService.getCustomerInfo(id);
    }

    @PutMapping("{id}")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody @Valid UpdateCustomerInfoRequest updateCustomerInfoRequest) {
        return customerService.updateCustomerInfo(id, updateCustomerInfoRequest);
    }

//    @DeleteMapping("{id}")
//    public UserView delete(@PathVariable String id) {
//        return userService.delete(new ObjectId(id));
//    }

    @PostMapping(value = "/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody @Valid ForgotPasswordRequest forgotPasswordRequest) throws MessagingException {
        Customer customer;
        try {
            customer = customerService.getCustomerByEmail(forgotPasswordRequest.getEmail());
        } catch (BadCredentialsException ex) {
            return ResponseEntity.badRequest().build();
        }

        ForgotPasswordResponse forgotPasswordResponse = customerService.constructEmailAndCreateResetToken(customer, forgotPasswordRequest);
        emailService.sendSimpleMessage(forgotPasswordResponse.getEmail(), forgotPasswordResponse.getSubject(), forgotPasswordResponse.getText());

        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/change-password")
    public ResponseEntity<?> changePassword(@RequestBody @Valid ChangePasswordRequest changePasswordRequest) {
        return customerService.changePassword(changePasswordRequest);
    }

    @PutMapping(value = "/update-password")
    public ResponseEntity<?> updatePassword(@RequestBody @Valid UpdatePasswordRequest updatePasswordRequest) {
        return customerService.updatePassword(updatePasswordRequest);
    }

//    @PostMapping(value = "/createAddress")
//    public ResponseEntity<?> createAddress(@RequestBody @Valid ChangePasswordRequest changePasswordRequest) {
//
//    }
//
//    @GetMapping(value = "/getAddress")
//    public ResponseEntity<?> getAddress(@RequestBody @Valid ChangePasswordRequest changePasswordRequest) {
//
//    }
//
//    @PutMapping(value = "/updateAddress")
//    public ResponseEntity<?> updateAddress(@RequestBody @Valid ChangePasswordRequest changePasswordRequest) {
//
//    }
//
//    @DeleteMapping(value = "/deleteAddress")
//    public ResponseEntity<?> deleteAddress(@RequestBody @Valid ChangePasswordRequest changePasswordRequest) {
//
//    }
}