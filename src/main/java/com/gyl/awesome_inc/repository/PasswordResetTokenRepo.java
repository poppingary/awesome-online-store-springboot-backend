package com.gyl.awesome_inc.repository;

import com.gyl.awesome_inc.domain.model.Customer;
import com.gyl.awesome_inc.domain.model.PasswordResetToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface PasswordResetTokenRepo extends CrudRepository<PasswordResetToken, String> {
    int countByCustomerAndExpiryDateIsAfter(Customer customer, Date now);
    int countByTokenAndExpiryDateIsAfter(String token, Date now);
}