package com.gyl.awesome_inc.api;

import com.gyl.awesome_inc.domain.dto.CreateAddressRequest;
import com.gyl.awesome_inc.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "api/admin/customer/address")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000", exposedHeaders = HttpHeaders.AUTHORIZATION)
public class AddressAdminApi {
    private final AddressService addressService;

    @PostMapping
    public ResponseEntity<?> createAddress(@RequestBody @Valid CreateAddressRequest createAddressRequest) {
        return addressService.create(createAddressRequest);
    }

    @GetMapping("{id}")
    public ResponseEntity<?> get(@PathVariable String id) {
        return addressService.get(id);
    }

//    @PutMapping("{id}")
//    public ResponseEntity<?> update(@PathVariable String id, @RequestBody @Valid UpdateCustomerInfoRequest updateCustomerInfoRequest) {
//        return addressService.update(id, updateCustomerInfoRequest);
//    }
//
//    @DeleteMapping("{id}")
//    public ResponseEntity<?>  delete(@PathVariable String id) {
//        return addressService.delete(new ObjectId(id));
//    }

    @GetMapping
    public ResponseEntity<?> getAddressByCustomerId(@RequestParam String customerId) {
        return addressService.getByCustomerId(customerId);
    }
}