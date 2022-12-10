package com.gyl.awesome_inc.service;

import com.gyl.awesome_inc.domain.dto.SearchResponse;
import com.gyl.awesome_inc.domain.model.Product;
import com.gyl.awesome_inc.repository.ProductRepo;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepo productRepo;
    private final ModelMapper modelMapper;

    public ResponseEntity<Product> get(String id) {
        Optional<Product> productOptional = productRepo.findById(id);
        if (productOptional.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().body(productOptional.get());
    }

    public ResponseEntity<Set<String>> getKeyword(String keyword) {
        List<Product> productList = productRepo.findByProductNameLike("%" + keyword.strip() + "%");
        Set<String> results = new TreeSet<>();
        for (Product product : productList) {
            results.add(product.getProductName());
        }

        return ResponseEntity.ok().body(results);
    }

    public ResponseEntity<List<SearchResponse>> search(String productName) {
        List<Product> productList = productRepo.findByProductNameLike("%" + productName.strip() + "%");

        List<SearchResponse> searchResponseList = new ArrayList<>();
        for (Product product : productList) {
            SearchResponse searchResponse = modelMapper.map(product, SearchResponse.class);
            searchResponse.setCategory(product.getCategory().getCategory());
            searchResponse.setSubcategory(product.getCategory().getSubcategory());
            searchResponseList.add(searchResponse);
        }

        searchResponseList = searchResponseList.stream()
                .sorted(Comparator.comparing(SearchResponse::getProductName))
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(searchResponseList);
    }
}