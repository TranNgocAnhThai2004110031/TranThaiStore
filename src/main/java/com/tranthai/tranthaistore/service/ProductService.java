package com.tranthai.tranthaistore.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import com.tranthai.tranthaistore.model.Product;

public interface ProductService {
    List<Product> getAllProduct();
    Page<Product> getAllProductPage(Pageable pageable);
    void addProduct(Product product);
    void removeProductById(Long id);
    Optional<Product> getProductById(long id);
    List<Product> getAllProductByCategory(long id);
    List<Product> getAllProductByBrand(Long id);
    List<Product> searchProduct(String keyword);
    Page<Product> searchProductPage(@Param("keyword") String keyword, Pageable pageable);
    Page<Product> getAllProductByCategoryPage(Long id, Pageable pageable);
    
}
