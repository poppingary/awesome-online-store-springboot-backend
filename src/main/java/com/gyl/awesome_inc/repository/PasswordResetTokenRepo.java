package com.gyl.awesome_inc.repository;

import com.gyl.awesome_inc.domain.model.Customer;
import com.gyl.awesome_inc.domain.model.PasswordResetToken;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;

public interface PasswordResetTokenRepo extends CrudRepository<PasswordResetToken, String> {
    int countByCustomerAndExpiryDateIsAfter(Customer customer, Date now);
    int countPasswordResetTokenAndExpiryDateIsAfter(String token, Date now);
}