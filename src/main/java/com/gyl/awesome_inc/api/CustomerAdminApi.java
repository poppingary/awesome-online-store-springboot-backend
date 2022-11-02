package com.gyl.awesome_inc.api;

import com.gyl.awesome_inc.domain.dto.RegisterRequest;
import com.gyl.awesome_inc.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "api/admin/customer")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:4200")
public class CustomerAdminApi {
    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody @Valid RegisterRequest registerRequest) {
        return customerService.create(registerRequest);
    }
}