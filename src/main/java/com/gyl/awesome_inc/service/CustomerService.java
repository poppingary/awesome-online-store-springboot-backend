package com.gyl.awesome_inc.service;

import com.gyl.awesome_inc.domain.dto.RegisterRequest;
import com.gyl.awesome_inc.domain.dto.RegisterResponse;
import com.gyl.awesome_inc.domain.model.Customer;
import com.gyl.awesome_inc.domain.model.SecurityQuestion;
import com.gyl.awesome_inc.domain.model.ShipAddress;
import com.gyl.awesome_inc.domain.model.ShipAddressId;
import com.gyl.awesome_inc.repository.CustomerRepo;
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

import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerService implements UserDetailsService {
    private final CustomerRepo customerRepo;
    private final PasswordEncoder bcryptEncoder;
    private final SecurityQuestionRepo securityQuestionRepo;
    private final ModelMapper modelMapper;
    private final ShipAddressRepo shipAddressRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Customer> customer = customerRepo.findByEmail(email);

        if (customer.isEmpty()) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        return new User(customer.get().getEmail(), customer.get().getPassword(), new ArrayList<>());
    }

    public Customer getCustomerByEmail(String email) {
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
}