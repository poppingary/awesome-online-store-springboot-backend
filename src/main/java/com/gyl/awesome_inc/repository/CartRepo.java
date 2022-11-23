package com.gyl.awesome_inc.repository;

import com.gyl.awesome_inc.domain.model.CustomerProduct;
import org.springframework.data.repository.CrudRepository;

public interface CartRepo extends CrudRepository<CustomerProduct, String> {

}