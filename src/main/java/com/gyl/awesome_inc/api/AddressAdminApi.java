package com.gyl.awesome_inc.api;

import com.gyl.awesome_inc.domain.dto.*;
import com.gyl.awesome_inc.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;

@RestController
@RequestMapping(path = "api/admin/customer/address")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000", exposedHeaders = HttpHeaders.AUTHORIZATION)
public class AddressAdminApi {
    private final AddressService addressService;

    @PostMapping
    public ResponseEntity<Set<CreateAddressResponse>> createAddress(@RequestBody @Valid CreateAddressRequest createAddressRequest) {
        return addressService.create(createAddressRequest);
    }

    @GetMapping("{id}")
    public ResponseEntity<GetAddressResponse> get(@PathVariable String id) {
        return addressService.get(id);
    }

    @PutMapping("{id}")
    public ResponseEntity<UpdateAddressResponse> update(@PathVariable String id, @RequestBody @Valid UpdateAddressRequest updateShipAddressRequest) {
        return addressService.update(id, updateShipAddressRequest);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        return addressService.delete(id);
    }

    @GetMapping
    public ResponseEntity<Set<GetAddressByCustomerIdResponse>> getAddressesByCustomerId(@RequestParam String customerId) {
        return addressService.getByCustomerId(customerId);
    }
}