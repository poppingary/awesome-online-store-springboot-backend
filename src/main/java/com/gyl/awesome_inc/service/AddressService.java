package com.gyl.awesome_inc.service;

import com.gyl.awesome_inc.domain.dto.CreateAddressRequest;
import com.gyl.awesome_inc.domain.model.Customer;
import com.gyl.awesome_inc.domain.model.ShipAddress;
import com.gyl.awesome_inc.domain.model.ShipAddressId;
import com.gyl.awesome_inc.repository.CustomerRepo;
import com.gyl.awesome_inc.repository.ShipAddressRepo;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final CustomerRepo customerRepo;
    private final ModelMapper modelMapper;
    private final ShipAddressRepo shipAddressRepo;

    @Transactional
    public ResponseEntity<?> create(CreateAddressRequest createAddressRequest) {
        String customerId = createAddressRequest.getCustomerId();
        Optional<Customer> customerOptional = customerRepo.findById(customerId);
        if (customerOptional.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        Customer customer = customerOptional.get();
        Set<ShipAddress> shipAddressSet = customer.getShipAddresses();
        if (createAddressRequest.getIsPrimary().equals("Y") && !shipAddressSet.isEmpty()) {
            for (ShipAddress address : shipAddressSet) {
                address.setIsPrimary("N");
            }
        }
        ShipAddress newShipAddress = modelMapper.map(createAddressRequest, ShipAddress.class);
        ShipAddressId shipAddressId = new ShipAddressId();
        shipAddressId.setCustomerId(customerId);
        shipAddressId.setShipAddressId(UUID.randomUUID().toString());
        newShipAddress.setId(shipAddressId);
        newShipAddress.setCustomer(customer);
        ShipAddress saveShipAddress = shipAddressRepo.save(newShipAddress);
        shipAddressSet.add(saveShipAddress);

        return ResponseEntity.ok().body(shipAddressSet);
    }

//    @GetMapping(value = "/getAddress")
//    public ResponseEntity<?> getAddress(@RequestBody @Valid ChangePasswordRequest changePasswordRequest) {
//
//    }
//
//    @PutMapping(value = "/updateAddress")
//    public ResponseEntity<?> updateAddress(@RequestBody @Valid ChangePasswordRequest changePasswordRequest) {
//
//    }
//
//    @DeleteMapping(value = "/deleteAddress")
//    public ResponseEntity<?> deleteAddress(@RequestBody @Valid ChangePasswordRequest changePasswordRequest) {
//
//    }
}