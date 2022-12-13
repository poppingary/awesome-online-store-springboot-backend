package com.gyl.awesome_inc.api;

import com.gyl.awesome_inc.domain.dto.*;
import com.gyl.awesome_inc.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "api/order")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000", exposedHeaders = HttpHeaders.AUTHORIZATION)
public class OrderApi {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<AddOrderResponse> create(@RequestBody @Valid AddOrderRequest addOrderRequest) {
        return orderService.create(addOrderRequest);
    }

    @PutMapping("return/{id}")
    public ResponseEntity<ReturnOrderResponse> update(@PathVariable String id, @RequestBody @Valid ReturnOrderRequest returnOrderRequest) {
        return orderService.update(id, returnOrderRequest);
    }

    @GetMapping
    public ResponseEntity<GetOrderResponse> getOrdersByCustomerId(@RequestParam String customerId) {
        return orderService.getByCustomerId(customerId);
    }
}