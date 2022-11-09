package com.gyl.awesome_inc.repository;

import com.gyl.awesome_inc.domain.model.Customer;
import com.gyl.awesome_inc.domain.model.PasswordResetToken;
import org.springframework.data.repository.CrudRepository;

public interface PasswordResetTokenRepo extends CrudRepository<PasswordResetToken, String> {
    PasswordResetToken findByCustomer(Customer customer);
    PasswordResetToken findByToken(String token);
    void deleteByCustomer(Customer customer);
}