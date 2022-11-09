package com.gyl.awesome_inc.api;

import com.gyl.awesome_inc.domain.dto.ForgotPasswordRequest;
import com.gyl.awesome_inc.domain.dto.ForgotPasswordResponse;
import com.gyl.awesome_inc.domain.dto.RegisterRequest;
import com.gyl.awesome_inc.domain.dto.ChangePasswordRequest;
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
@CrossOrigin("http://localhost:4200")
public class CustomerAdminApi {
    private final CustomerService customerService;
    private final EmailService emailService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody @Valid RegisterRequest registerRequest) {
        return customerService.create(registerRequest);
    }

    @PostMapping(value = "/forgotPassword")
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

    @PostMapping(value = "/changePassword")
    public ResponseEntity<?> changePassword(@RequestBody @Valid ChangePasswordRequest changePasswordRequest) {
        return customerService.changePassword(changePasswordRequest);
    }

//    @PutMapping("{id}")
//    public UserView update(@PathVariable String id, @RequestBody @Valid UpdateUserRequest request) {
//        return userService.update(new ObjectId(id), request);
//    }
//
//    @DeleteMapping("{id}")
//    public UserView delete(@PathVariable String id) {
//        return userService.delete(new ObjectId(id));
//    }
//
//    @GetMapping("{id}")
//    public UserView get(@PathVariable String id) {
//        return userService.getUser(new ObjectId(id));
//    }
}