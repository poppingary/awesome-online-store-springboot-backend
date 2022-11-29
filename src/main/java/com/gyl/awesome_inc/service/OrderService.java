package com.gyl.awesome_inc.service;

import com.gyl.awesome_inc.domain.dto.*;
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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

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
        BigDecimal actualPrice = product.getUnitPrice().multiply(BigDecimal.ONE.subtract(product.getDiscount())).setScale(2, RoundingMode.UP);
        orderProduct.setActualPrice(actualPrice);

        orderProductRepo.save(orderProduct);
    }

    private AddOrderResponse createAddOrderResponse(Order order) {
        AddOrderResponse addOrderResponse = new AddOrderResponse();
        addOrderResponse.setOrderId(order.getId());
        String arrivingDate = getArrivingDate(order);
        addOrderResponse.setArrivingDate(arrivingDate);

        return addOrderResponse;
    }

    private String getArrivingDate(Order order) {
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

        return formatter.format(orderDate);
    }

    public ResponseEntity<?> getByCustomerId(String customerId) {
        Optional<Customer> customerOptional = customerRepo.findById(customerId);
        if (customerOptional.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        Customer customer = customerOptional.get();
        Set<OrderResponse> orderResponseSet = new HashSet<>();
        Set<Order> orderSet = customer.getOrders();
        for (Order order : orderSet) {
            OrderResponse orderResponse = modelMapper.map(order, OrderResponse.class);
            Set<OrderProduct> orderProductSet = order.getOrderProducts();
            Set<ProductResponse> productResponseSet = new HashSet<>();
            for (OrderProduct orderProduct : orderProductSet) {
                ProductResponse productResponse = new ProductResponse();
                productResponse.setProductName(orderProduct.getProduct().getProductName());
                productResponse.setQuantity(orderProduct.getQuantity());
                productResponse.setActualPrice(orderProduct.getActualPrice());
                productResponseSet.add(productResponse);
            }
            orderResponse.setProductResponseSet(productResponseSet);
            String arrivingDate = getArrivingDate(order);
            orderResponse.setArrivingDate(arrivingDate);
            BigDecimal totalPrice = getTotalPrice(orderProductSet);
            orderResponse.setTotalPrice(totalPrice);
            orderResponseSet.add(orderResponse);

        }
        GetOrderResponse getOrderResponse = new GetOrderResponse();
        getOrderResponse.setOrderSet(orderResponseSet);

        return ResponseEntity.ok().body(getOrderResponse);
    }

    private BigDecimal getTotalPrice(Set<OrderProduct> orderProductSet) {
        BigDecimal sum = BigDecimal.ZERO;

        for (OrderProduct orderProduct : orderProductSet) {
            BigDecimal temp = orderProduct.getActualPrice().multiply(BigDecimal.valueOf(orderProduct.getQuantity()));
            sum = sum.add(temp);
        }

        return sum.setScale(2, RoundingMode.UP);
    }
}