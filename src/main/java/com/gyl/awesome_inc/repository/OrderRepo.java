package com.gyl.awesome_inc.repository;

import com.gyl.awesome_inc.domain.model.Order;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepo extends CrudRepository<Order, String> {

}