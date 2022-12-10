package com.gyl.awesome_inc.api;

import com.gyl.awesome_inc.domain.dto.SearchResponse;
import com.gyl.awesome_inc.domain.model.Product;
import com.gyl.awesome_inc.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(path = "api/product")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000", exposedHeaders = HttpHeaders.AUTHORIZATION)
public class ProductApi {
    private final ProductService productService;

    @GetMapping("{id}")
    public ResponseEntity<Product> get(@PathVariable String id) {
        return productService.get(id);
    }

    @GetMapping("/keyword")
    public ResponseEntity<Set<String>> getKeyword(@RequestParam String q) {
        return productService.getKeyword(q);
    }

    @GetMapping("/search")
    public ResponseEntity<List<SearchResponse>> search(@RequestParam String q) {
        return productService.search(q);
    }
}