package com.gyl.awesome_inc.service;

import com.gyl.awesome_inc.domain.dto.AddOrderRequest;
import com.gyl.awesome_inc.domain.dto.AddOrderResponse;
import com.gyl.awesome_inc.domain.dto.ProductQuantity;
import com.gyl.awesome_inc.domain.model.*;
import com.gyl.awesome_inc.repository.CustomerRepo;
import com.gyl.awesome_inc.repository.OrderProductRepo;
import com.gyl.awesome_inc.repository.OrderRepo;
import com.gyl.awesome_inc.repository.ProductRepo;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {
    private static final String PATTERN_FORMAT = "MM/dd/yyyy";
    private final CustomerRepo customerRepo;
    private final ModelMapper modelMapper;
    private final ProductRepo productRepo;
    private final PasswordEncoder bcryptEncoder;
    private final OrderRepo orderRepo;
    private final OrderProductRepo orderProductRepo;

    @Transactional
    public ResponseEntity<?> create(AddOrderRequest addOrderRequest) {
        String customerId = addOrderRequest.getCustomerId();
        Optional<Customer> customerOptional = customerRepo.findById(customerId);
        if (customerOptional.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        Customer customer = customerOptional.get();

        List<ProductQuantity> orderProductList = addOrderRequest.getProductQuantityList();
        for (ProductQuantity productQuantity : orderProductList) {
            String productId = productQuantity.getProductId();
            Optional<Product> productOptional = productRepo.findById(productId);
            if (productOptional.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            int quantityInStock = productOptional.get().getQuantity();
            if (Integer.parseInt(productQuantity.getQuantity()) > quantityInStock) {
                return ResponseEntity.badRequest().build();
            }
        }

        Order saveOrder = saveOrder(addOrderRequest, customer);
        for (ProductQuantity productQuantity : orderProductList) {
            String productId = productQuantity.getProductId();
            Optional<Product> productOptional = productRepo.findById(productId);
            if (productOptional.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            Product saveProduct = saveProduct(productOptional.get(), productQuantity);
            saveOrderProduct(saveOrder, saveProduct, productQuantity);
        }

        AddOrderResponse addOrderResponse = createAddOrderResponse(saveOrder);

        return ResponseEntity.ok().body(addOrderResponse);
    }

    private Order saveOrder(AddOrderRequest addOrderRequest, Customer customer) {
        Order order = modelMapper.map(addOrderRequest, Order.class);
        order.setCustomer(customer);
        order.setOrderDate(LocalDateTime
                .now()
                .atZone(ZoneOffset.of("+00:00"))
                .toInstant());
        order.setIsReturned("N");
        order.setCreditCardCvv(bcryptEncoder.encode(addOrderRequest.getCreditCardCvv()));

        return orderRepo.save(order);
    }

    private Product saveProduct(Product product, ProductQuantity productQuantity) {
        int requestQuantity = Integer.parseInt(productQuantity.getQuantity());
        int quantityInStock = product.getQuantity();
        product.setQuantity(quantityInStock - requestQuantity);

        return productRepo.save(product);
    }

    private void saveOrderProduct(Order order, Product product, ProductQuantity productQuantity) {
        OrderProduct orderProduct = new OrderProduct();
        OrderProductId orderProductId = new OrderProductId();
        orderProductId.setOrderId(order.getId());
        orderProductId.setProductId(product.getId());
        orderProduct.setId(orderProductId);
        orderProduct.setOrder(order);
        orderProduct.setProduct(product);
        orderProduct.setQuantity(Integer.parseInt(productQuantity.getQuantity()));

        orderProductRepo.save(orderProduct);
    }

    private AddOrderResponse createAddOrderResponse(Order order) {
        AddOrderResponse addOrderResponse = new AddOrderResponse();
        addOrderResponse.setOrderId(order.getId());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PATTERN_FORMAT)
                .withLocale(Locale.US)
                .withZone(ZoneOffset.of("+00:00"));
        Instant orderDate = order.getOrderDate();
        String shipMode = order.getShipMode();
        switch (shipMode) {
            case "Same Day" -> orderDate = orderDate.plus(0, ChronoUnit.DAYS);
            case "First Class" -> orderDate = orderDate.plus(2, ChronoUnit.DAYS);
            case "Second Class" -> orderDate = orderDate.plus(4, ChronoUnit.DAYS);
            case "Standard Class" -> orderDate = orderDate.plus(6, ChronoUnit.DAYS);
        }
        String arrivingDate = formatter.format(orderDate);
        addOrderResponse.setArrivingDate(arrivingDate);

        return addOrderResponse;
    }
}