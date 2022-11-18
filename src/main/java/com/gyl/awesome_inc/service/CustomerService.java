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
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerService implements UserDetailsService {
    private final EmailService emailService;
    private final CustomerRepo customerRepo;
    private final PasswordEncoder bcryptEncoder;
    private final SecurityQuestionRepo securityQuestionRepo;
    private final ShipAddressRepo shipAddressRepo;
    private final PasswordResetTokenRepo passwordResetTokenRepo;
    private final ModelMapper modelMapper;

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
        Customer customer = createNewCustomer(registerRequest);
        Customer saveCustomer = customerRepo.save(customer);

        ShipAddress shipAddress = createShipAddress(registerRequest, saveCustomer);
        ShipAddress saveShipAddress = shipAddressRepo.save(shipAddress);

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

    private ShipAddress createShipAddress(RegisterRequest registerRequest, Customer customer) {
        ShipAddressId newShipAddressId = new ShipAddressId();
        newShipAddressId.setShipAddressId(UUID.randomUUID().toString());

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

    public ResponseEntity<?> get(String customerId) {
        Optional<Customer> customerOptional = customerRepo.findById(customerId);
        if (customerOptional.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        GetCustomerInfoResponse getCustomerInfoResponse = modelMapper.map(customerOptional.get(), GetCustomerInfoResponse.class);
        getCustomerInfoResponse.setSecurityQuestion(customerOptional.get().getSecurityQuestion().getSecurityQuestion());

        return ResponseEntity.ok().body(getCustomerInfoResponse);
    }

    @Transactional
    public ResponseEntity<?> update(String customerId, UpdateCustomerInfoRequest updateCustomerInfoRequest) {
        Optional<Customer> customerOptional = customerRepo.findById(customerId);
        if (customerOptional.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        Customer customer = customerOptional.get();
        customer.setEmail(updateCustomerInfoRequest.getEmail());
        customer.setFirstName(updateCustomerInfoRequest.getFirstName());
        customer.setLastName(updateCustomerInfoRequest.getLastName());
        String securityQuestionString = updateCustomerInfoRequest.getSecurityQuestion();
        Optional<SecurityQuestion> securityQuestionOptional = securityQuestionRepo.findBySecurityQuestion(securityQuestionString);
        if (securityQuestionOptional.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        customer.setSecurityQuestion(securityQuestionOptional.get());
        customer.setSecurityAnswer(updateCustomerInfoRequest.getSecurityAnswer());

        Customer saveCustomer = customerRepo.save(customer);
        UpdateCustomerInfoResponse updateCustomerInfoResponse = modelMapper.map(saveCustomer, UpdateCustomerInfoResponse.class);
        updateCustomerInfoResponse.setSecurityQuestion(securityQuestionString);

        return ResponseEntity.ok().body(updateCustomerInfoResponse);
    }

    @Transactional
    public ResponseEntity<?> forgotPassword(ForgotPasswordRequest forgotPasswordRequest) throws MessagingException {
        Customer customer;
        try {
            customer = getCustomerByEmail(forgotPasswordRequest.getEmail());
        } catch (BadCredentialsException ex) {
            return ResponseEntity.badRequest().build();
        }

        ForgotPasswordResponse forgotPasswordResponse = constructEmailAndCreateResetToken(customer, forgotPasswordRequest);
        emailService.sendSimpleMessage(forgotPasswordResponse.getEmail(), forgotPasswordResponse.getSubject(), forgotPasswordResponse.getText());

        return ResponseEntity.ok().build();
    }

    private ForgotPasswordResponse constructEmailAndCreateResetToken(Customer customer, ForgotPasswordRequest forgotPasswordRequest) {
        String subject = "Reset password";
        String token = UUID.randomUUID().toString();
        String text = constructText(token, forgotPasswordRequest);
        createResetToken(customer, token);

        return new ForgotPasswordResponse(forgotPasswordRequest.getEmail(), subject, text);
    }

    private String constructText(String token, ForgotPasswordRequest forgotPasswordRequest) {
        String clientUri = forgotPasswordRequest.getClientUri();
        String resetUrl = clientUri + "?token=" + token;
        return "<p>Hello,</p>" +
                "<p>You have requested to reset your password.</p>" +
                "<p>Click the link below to change your password:</p>" +
                "<p><a href=\"" + resetUrl + "\">Change my password</a></p>" +
                "<br>" +
                "<p>Ignore this email if you do remember your password, " +
                "or you have not made the request.</p>";
    }

    private void createResetToken(Customer customer, String token) {
        Optional<PasswordResetToken> passwordResetTokenOptional = passwordResetTokenRepo.findByCustomer(customer);
        PasswordResetToken passwordResetToken = passwordResetTokenOptional.orElseGet(PasswordResetToken::new);
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
        String token = changePasswordRequest.getResetToken();
        Optional<PasswordResetToken> passwordResetTokenOptional = passwordResetTokenRepo.findByToken(token);
        if (passwordResetTokenOptional.isEmpty() || !isResetTokenValid(passwordResetTokenOptional.get())) {
            return ResponseEntity.badRequest().build();
        }
        Optional<Customer> customerOptional = customerRepo.findById(passwordResetTokenOptional.get().getCustomer().getId());
        if (customerOptional.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        saveNewPassword(customerOptional.get(), changePasswordRequest.getNewPassword());

        return ResponseEntity.ok().build();
    }

    private boolean isResetTokenValid(PasswordResetToken passwordResetToken) {
        LocalDateTime expiryDateLocal = passwordResetToken.getExpiryDate().toInstant()
                .atZone(ZoneOffset.of("+00:00"))
                .toLocalDateTime();

        return LocalDateTime.now().isBefore(expiryDateLocal);
    }

    public ResponseEntity<?> updatePassword(UpdatePasswordRequest updatePasswordRequest) {
        Optional<Customer> customerOptional = customerRepo.findById(updatePasswordRequest.getCustomerId());
        if (customerOptional.isEmpty() ||
                !bcryptEncoder.matches(updatePasswordRequest.getOldPassword(), customerOptional.get().getPassword())) {
            return ResponseEntity.badRequest().build();
        }

        saveNewPassword(customerOptional.get(), updatePasswordRequest.getNewPassword());

        return ResponseEntity.ok().build();
    }

    private void saveNewPassword(Customer customer, String newPassword) {
        customer.setPassword(bcryptEncoder.encode(newPassword));
        customerRepo.save(customer);
    }
}