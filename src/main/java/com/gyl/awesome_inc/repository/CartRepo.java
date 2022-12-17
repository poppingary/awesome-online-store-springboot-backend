package com.gyl.awesome_inc.repository;

import com.gyl.awesome_inc.domain.model.Customer;
import com.gyl.awesome_inc.domain.model.CustomerProduct;
import com.gyl.awesome_inc.domain.model.CustomerProductId;
import org.springframework.data.repository.CrudRepository;

public interface CartRepo extends CrudRepository<CustomerProduct, CustomerProductId> {
    void deleteByCustomer(Customer customer);
}