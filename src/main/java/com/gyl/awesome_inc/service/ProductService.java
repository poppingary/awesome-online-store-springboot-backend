package com.gyl.awesome_inc.service;

import com.gyl.awesome_inc.domain.model.Product;
import com.gyl.awesome_inc.repository.ProductRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepo productRepo;

    public ResponseEntity<?> getKeyword(String keyword) {
        List<Product> results = productRepo.findByProductNameLike("%" + keyword + "%");
        Set<String> distinctResults = new HashSet<>();
        for (Product product : results) {
            distinctResults.add(product.getProductName());
        }

        return ResponseEntity.ok().body(distinctResults);
    }
}