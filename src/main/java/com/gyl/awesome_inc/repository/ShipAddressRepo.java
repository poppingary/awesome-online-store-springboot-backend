package com.gyl.awesome_inc.repository;

import com.gyl.awesome_inc.domain.model.ShipAddress;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShipAddressRepo extends CrudRepository<ShipAddress, String> {

}
