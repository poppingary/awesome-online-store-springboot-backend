package com.gyl.awesome_inc.repository;

import com.gyl.awesome_inc.domain.model.Customer;
import com.gyl.awesome_inc.domain.model.PasswordResetToken;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PasswordResetTokenRepo extends CrudRepository<PasswordResetToken, String> {
    Optional<PasswordResetToken> findByCustomer(Customer customer);
    Optional<PasswordResetToken> findByToken(String token);
    void deleteByCustomer(Customer customer);
}