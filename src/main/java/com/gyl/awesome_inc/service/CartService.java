package com.gyl.awesome_inc.service;

import com.gyl.awesome_inc.domain.dto.*;
import com.gyl.awesome_inc.domain.model.Customer;
import com.gyl.awesome_inc.domain.model.CustomerProduct;
import com.gyl.awesome_inc.domain.model.CustomerProductId;
import com.gyl.awesome_inc.repository.CartRepo;
import com.gyl.awesome_inc.repository.CustomerRepo;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CartService {
    private final ModelMapper modelMapper;
    private final CartRepo cartRepo;
    private final CustomerRepo customerRepo;

    public ResponseEntity<AddToCartResponse> create(AddToCartRequest addToCartRequest) {
        CustomerProduct customerProduct = modelMapper.map(addToCartRequest, CustomerProduct.class);
        customerProduct.getId().setCustomerId(addToCartRequest.getCustomerId());
        customerProduct.getId().setProductId(addToCartRequest.getProductId());

        CustomerProduct saveCustomerProduct = cartRepo.save(customerProduct);
        AddToCartResponse addToCartResponse = new AddToCartResponse(saveCustomerProduct.getId().getProductId(), String.valueOf(saveCustomerProduct.getQuantity()));

        return ResponseEntity.ok().body(addToCartResponse);
    }

    public ResponseEntity<List<GetCartResponse>> get(String customerId) {
        Optional<Customer> customerOptional = customerRepo.findById(customerId);
        if (customerOptional.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Customer customer = customerOptional.get();
        Set<CustomerProduct> customerProductSet = customer.getCustomerProducts();
        List<GetCartResponse> getCartResponseList = new ArrayList<>();
        for (CustomerProduct customerProduct : customerProductSet) {
            GetCartResponse getCartResponse = new GetCartResponse();
            getCartResponse.setProductId(customerProduct.getId().getProductId());
            getCartResponse.setProductName(customerProduct.getProduct().getProductName());
            getCartResponse.setQuantity(String.valueOf(customerProduct.getQuantity()));
            getCartResponse.setUnitPrice(String.valueOf(customerProduct.getProduct().getUnitPrice()));
            getCartResponse.setDiscount(String.valueOf(customerProduct.getProduct().getDiscount()));
            getCartResponseList.add(getCartResponse);
        }


        return ResponseEntity.ok().body(getCartResponseList);
    }

    public ResponseEntity<UpdateCartResponse> update(String customerId, UpdateCartRequest updateCartRequest) {
        CustomerProductId customerProductId = new CustomerProductId();
        customerProductId.setCustomerId(customerId);
        customerProductId.setProductId(updateCartRequest.getProductId());
        Optional<CustomerProduct> customerProductOptional = cartRepo.findById(customerProductId);
        if (customerProductOptional.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        CustomerProduct customerProduct = customerProductOptional.get();
        customerProduct.setQuantity(Integer.valueOf(updateCartRequest.getQuantity()));
        CustomerProduct saveCustomerProduct = cartRepo.save(customerProduct);
        UpdateCartResponse updateCartResponse = new UpdateCartResponse();
        updateCartResponse.setProductId(saveCustomerProduct.getId().getProductId());
        updateCartResponse.setQuantity(String.valueOf(saveCustomerProduct.getQuantity()));

        return ResponseEntity.ok().body(updateCartResponse);
    }

    public ResponseEntity<?> delete(String customerId, DeleteCartRequest deleteCartRequest) {
        CustomerProductId customerProductId = new CustomerProductId();
        customerProductId.setCustomerId(customerId);
        customerProductId.setProductId(deleteCartRequest.getProductId());
        cartRepo.deleteById(customerProductId);
        return ResponseEntity.ok().build();
    }
}