package com.gyl.awesome_inc.repository;

import com.gyl.awesome_inc.domain.model.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepo extends CrudRepository<Product, String> {
    List<Product> findByProductNameLike(String keyword);
}