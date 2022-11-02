package com.gyl.awesome_inc.service;

import com.gyl.awesome_inc.domain.model.Customer;
import com.gyl.awesome_inc.repository.CustomerRepo;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerService implements UserDetailsService {
    private final CustomerRepo customerRepo;

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
}