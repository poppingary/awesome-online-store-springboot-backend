package com.gyl.awesome_inc.api;

import com.gyl.awesome_inc.domain.dto.AddToCartRequest;
import com.gyl.awesome_inc.domain.dto.DeleteCartRequest;
import com.gyl.awesome_inc.domain.dto.UpdateCartRequest;
import com.gyl.awesome_inc.service.CartService;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "api/cart")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000", exposedHeaders = HttpHeaders.AUTHORIZATION)
public class CartApi {
    private final CartService cartService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody @Valid AddToCartRequest addToCartRequest) {
        return cartService.create(addToCartRequest);
    }

    @GetMapping("{id}")
    public ResponseEntity<?> get(@PathVariable String id) {
        return cartService.get(id);
    }

    @PutMapping("{id}")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody @Valid UpdateCartRequest updateCartRequest) {
        return cartService.update(id, updateCartRequest);
    }
    
    @DeleteMapping("{id}")
    public ResponseEntity<?> delete(@PathVariable String id, @RequestBody @Valid DeleteCartRequest deleteCartRequest) {
        return cartService.delete(id, deleteCartRequest);
    }
}