package com.gyl.awesome_inc.api;

import com.gyl.awesome_inc.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/product")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000", exposedHeaders = HttpHeaders.AUTHORIZATION)
public class ProductApi {
    private final ProductService productService;

    @GetMapping("/keyword")
    public ResponseEntity<?> getKeyword(@RequestParam String q) {
        return productService.getKeyword(q);
    }

    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam String q) {
        return productService.search(q);
    }
}