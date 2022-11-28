package com.gyl.awesome_inc.repository;

import com.gyl.awesome_inc.domain.model.OrderProduct;
import com.gyl.awesome_inc.domain.model.OrderProductId;
import org.springframework.data.repository.CrudRepository;

public interface OrderProductRepo extends CrudRepository<OrderProduct, OrderProductId> {

}