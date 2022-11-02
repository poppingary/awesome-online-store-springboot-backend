package com.gyl.awesome_inc.repository;

import com.gyl.awesome_inc.domain.model.Customer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepo extends CrudRepository<Customer, String> {
    Optional<Customer> findByEmail(String username);
}