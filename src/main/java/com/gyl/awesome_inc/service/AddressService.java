package com.gyl.awesome_inc.service;

import com.gyl.awesome_inc.domain.dto.*;
import com.gyl.awesome_inc.domain.model.Customer;
import com.gyl.awesome_inc.domain.model.ShipAddress;
import com.gyl.awesome_inc.repository.ShipAddressRepo;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final CustomerService customerService;
    private final ModelMapper modelMapper;
    private final ShipAddressRepo shipAddressRepo;

    @Transactional
    public ResponseEntity<Set<CreateAddressResponse>> create(CreateAddressRequest createAddressRequest) {
        Customer customer = customerService.getCustomerById(createAddressRequest.getCustomerId());

        Set<ShipAddress> shipAddressSet = customer.getShipAddresses();
        ShipAddress saveShipAddress = saveNewShipAddress(createAddressRequest, shipAddressSet, customer);
        shipAddressSet.add(saveShipAddress);
        Set<CreateAddressResponse> createAddressResponseSet = new HashSet<>();
        for (ShipAddress address : shipAddressSet) {
            CreateAddressResponse createAddressResponse = modelMapper.map(address, CreateAddressResponse.class);
            createAddressResponse.setShipAddressId(address.getId());
            createAddressResponseSet.add(createAddressResponse);
        }

        return ResponseEntity.ok().body(createAddressResponseSet);
    }

    private ShipAddress saveNewShipAddress(CreateAddressRequest createAddressRequest, Set<ShipAddress> shipAddressSet, Customer customer) {
        setPrimaryAddress(createAddressRequest.getIsPrimary(), shipAddressSet);
        ShipAddress shipAddress = createNewShipAddress(createAddressRequest, customer);

        return shipAddressRepo.save(shipAddress);
    }

    private void setPrimaryAddress(String isPrimary, Set<ShipAddress> shipAddressSet) {
        if (isPrimary.equals("Y") && !shipAddressSet.isEmpty()) {
            for (ShipAddress address : shipAddressSet) {
                address.setIsPrimary("N");
            }
        }
    }

    private ShipAddress createNewShipAddress(CreateAddressRequest createAddressRequest, Customer customer) {
        ShipAddress shipAddress = modelMapper.map(createAddressRequest, ShipAddress.class);
        shipAddress.setId(UUID.randomUUID().toString());
        shipAddress.setCustomer(customer);

        return shipAddress;
    }

    public ResponseEntity<GetAddressResponse> get(String id) {
        Optional<ShipAddress> shipAddressOptional = shipAddressRepo.findById(id);
        if (shipAddressOptional.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        ShipAddress shipAddress = shipAddressOptional.get();
        GetAddressResponse getAddressResponse = modelMapper.map(shipAddress, GetAddressResponse.class);

        return ResponseEntity.ok().body(getAddressResponse);
    }

    public ResponseEntity<UpdateAddressResponse> update(String id, UpdateAddressRequest updateShipAddressRequest) {
        Optional<ShipAddress> shipAddressOptional = shipAddressRepo.findById(id);
        if (shipAddressOptional.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        ShipAddress shipAddress = shipAddressOptional.get();
        Customer customer = shipAddress.getCustomer();
        Set<ShipAddress> shipAddressSet = customer.getShipAddresses();
        setPrimaryAddress(updateShipAddressRequest.getIsPrimary(), shipAddressSet);
        shipAddress.setPostalCode(updateShipAddressRequest.getPostalCode());
        shipAddress.setCity(updateShipAddressRequest.getCity());
        shipAddress.setState(updateShipAddressRequest.getState());
        shipAddress.setCountry(updateShipAddressRequest.getCountry());
        shipAddress.setRegion(updateShipAddressRequest.getRegion());
        shipAddress.setMarket(updateShipAddressRequest.getMarket());
        shipAddress.setIsPrimary(updateShipAddressRequest.getIsPrimary());
        shipAddress.setCustomer(customer);
        ShipAddress saveShipAddress = shipAddressRepo.save(shipAddress);
        UpdateAddressResponse updateAddressResponse = modelMapper.map(saveShipAddress, UpdateAddressResponse.class);

        return ResponseEntity.ok().body(updateAddressResponse);
    }

    public ResponseEntity<?> delete(String id) {
        shipAddressRepo.deleteById(id);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<Set<GetAddressByCustomerIdResponse>> getByCustomerId(String id) {
        Customer customer = customerService.getCustomerById(id);

        Set<ShipAddress> shipAddressSet = customer.getShipAddresses();

        Set<GetAddressByCustomerIdResponse> getAddressByCustomerIdResponseSet = new HashSet<>();
        for (ShipAddress address : shipAddressSet) {
            GetAddressByCustomerIdResponse getAddressByCustomerIdResponse = modelMapper.map(address, GetAddressByCustomerIdResponse.class);
            getAddressByCustomerIdResponse.setShipAddressId(address.getId());
            getAddressByCustomerIdResponseSet.add(getAddressByCustomerIdResponse);
        }

        return ResponseEntity.ok().body(getAddressByCustomerIdResponseSet);
    }
}