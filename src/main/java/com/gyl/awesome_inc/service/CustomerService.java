package com.gyl.awesome_inc.service;

import com.gyl.awesome_inc.domain.dto.*;
import com.gyl.awesome_inc.domain.model.*;
import com.gyl.awesome_inc.repository.CustomerRepo;
import com.gyl.awesome_inc.repository.PasswordResetTokenRepo;
import com.gyl.awesome_inc.repository.SecurityQuestionRepo;
import com.gyl.awesome_inc.repository.ShipAddressRepo;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerService implements UserDetailsService {
    private final CustomerRepo customerRepo;
    private final PasswordEncoder bcryptEncoder;
    private final SecurityQuestionRepo securityQuestionRepo;
    private final ModelMapper modelMapper;
    private final ShipAddressRepo shipAddressRepo;
    private final PasswordResetTokenRepo passwordResetTokenRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Customer> customer = customerRepo.findByEmail(email);

        if (customer.isEmpty()) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        return new User(customer.get().getEmail(), customer.get().getPassword(), new ArrayList<>());
    }

    public Customer getCustomerByEmail(String email) throws UsernameNotFoundException {
        Optional<Customer> customer = customerRepo.findByEmail(email);

        if (customer.isEmpty()) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        return customer.get();
    }

    @Transactional
    public ResponseEntity<?> create(RegisterRequest registerRequest) {
        Customer newCustomer = createNewCustomer(registerRequest);
        Customer saveCustomer = customerRepo.save(newCustomer);

        ShipAddress newShipAddress = createNewShipAddress(registerRequest, saveCustomer);
        ShipAddress saveShipAddress = shipAddressRepo.save(newShipAddress);

        RegisterResponse registerResponse = createRegisterResponse(saveCustomer, saveShipAddress);

        return ResponseEntity.ok().body(registerResponse);
    }

    private Customer createNewCustomer(RegisterRequest registerRequest) {
        Customer newCustomer = modelMapper.map(registerRequest, Customer.class);
        String encodePassword = bcryptEncoder.encode(registerRequest.getPassword());
        newCustomer.setPassword(encodePassword);
        SecurityQuestion securityQuestion = null;
        if (securityQuestionRepo.findBySecurityQuestion(registerRequest.getSecurityQuestion()).isPresent()) {
            securityQuestion = securityQuestionRepo.findBySecurityQuestion(registerRequest.getSecurityQuestion()).get();
        }
        newCustomer.setSecurityQuestion(securityQuestion);

        return newCustomer;
    }

    private ShipAddress createNewShipAddress(RegisterRequest registerRequest, Customer customer) {
        long numOfAddress = shipAddressRepo.countByCustomer(customer);
        ShipAddressId newShipAddressId = new ShipAddressId();
        newShipAddressId.setShipAddressId(String.valueOf(numOfAddress + 1));

        ShipAddress newShipAddress = modelMapper.map(registerRequest, ShipAddress.class);
        newShipAddress.setId(newShipAddressId);
        newShipAddress.setCustomer(customer);

        return newShipAddress;
    }

    private RegisterResponse createRegisterResponse(Customer customer, ShipAddress shipAddress) {
        RegisterResponse registerResponse = modelMapper.map(customer, RegisterResponse.class);

        registerResponse.setPostalCode(shipAddress.getPostalCode());
        registerResponse.setMarket(shipAddress.getMarket());
        registerResponse.setRegion(shipAddress.getRegion());
        registerResponse.setCountry(shipAddress.getCountry());
        registerResponse.setState(shipAddress.getState());
        registerResponse.setCity(shipAddress.getCity());
        registerResponse.setIsPrimary(shipAddress.getIsPrimary());

        return registerResponse;
    }

    @Transactional
    public ForgotPasswordResponse constructEmailAndCreateResetToken(Customer customer, ForgotPasswordRequest forgotPasswordRequest) {
        String subject = "Reset password";
        String token = UUID.randomUUID().toString();
        String text = constructEmail(token, forgotPasswordRequest);
        createResetToken(customer, token);

        return new ForgotPasswordResponse(forgotPasswordRequest.getEmail(), subject, text);
    }

    private String constructEmail(String token, ForgotPasswordRequest forgotPasswordRequest) {
        String clientUri = forgotPasswordRequest.getClientUri();
        String resetUrl = new StringBuilder().append(clientUri).append("?token=").append(token).toString();
        return new StringBuilder()
                .append("<p>Hello,</p>")
                .append("<p>You have requested to reset your password.</p>")
                .append("<p>Click the link below to change your password:</p>")
                .append("<p><a href=\"").append(resetUrl).append("\">Change my password</a></p>")
                .append("<br>")
                .append("<p>Ignore this email if you do remember your password, ")
                .append("or you have not made the request.</p>").toString();
    }

    private void createResetToken(Customer customer, String token) {
        PasswordResetToken passwordResetToken = passwordResetTokenRepo.findByCustomer(customer);
        if (!ObjectUtils.isEmpty(passwordResetToken)) {
            passwordResetTokenRepo.deleteByCustomer(customer);
        }
        passwordResetToken = new PasswordResetToken();
        passwordResetToken.setToken(token);
        passwordResetToken.setCustomer(customer);
        passwordResetToken.setExpiryDate(Date.from(
                LocalDateTime
                        .now()
                        .plusDays(1)
                        .atZone(ZoneOffset.of("+00:00"))
                        .toInstant()
        ));
        passwordResetTokenRepo.save(passwordResetToken);
    }

    @Transactional
    public ResponseEntity<?> changePassword(ChangePasswordRequest changePasswordRequest) {
        String token = changePasswordRequest.getResetToke();
        if (!isResetTokenValid(token)) {
            return ResponseEntity.badRequest().build();
        }

        String newPassword = bcryptEncoder.encode(changePasswordRequest.getNewPassword());
        PasswordResetToken passwordResetToken = passwordResetTokenRepo.findByToken(token);
        Customer customer = customerRepo.findById(passwordResetToken.getCustomer().getCustomerId()).get();
        customer.setPassword(newPassword);
        customerRepo.save(customer);

        return ResponseEntity.ok().build();
    }

    private boolean isResetTokenValid(String token) {
        PasswordResetToken passwordResetToken = passwordResetTokenRepo.findByToken(token);
        LocalDateTime expiryDateLocal = passwordResetToken.getExpiryDate().toInstant()
                .atZone(ZoneOffset.of("+00:00"))
                .toLocalDateTime();

        return LocalDateTime.now().isBefore(expiryDateLocal);
    }
}